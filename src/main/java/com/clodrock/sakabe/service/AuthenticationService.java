package com.clodrock.sakabe.service;

import com.clodrock.sakabe.configuration.JwtAuthenticationFilter;
import com.clodrock.sakabe.entity.SakaUser;
import com.clodrock.sakabe.entity.Token;
import com.clodrock.sakabe.enums.TokenType;
import com.clodrock.sakabe.exception.InvalidAuthenticationException;
import com.clodrock.sakabe.exception.NotFoundException;
import com.clodrock.sakabe.exception.PasswordsDontMatchException;
import com.clodrock.sakabe.exception.UserAlreadyExistException;
import com.clodrock.sakabe.model.AuthenticationRequest;
import com.clodrock.sakabe.model.AuthenticationResponse;
import com.clodrock.sakabe.model.RegisterRequest;
import com.clodrock.sakabe.repository.TokenRepository;
import com.clodrock.sakabe.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final JwtAuthenticationFilter authenticationFilter;

    public AuthenticationResponse register(RegisterRequest request) {
        repository.findByEmail(request.getEmail())
                .ifPresent(existingUser -> {
                    throw new UserAlreadyExistException("User already exists!");
                });

        if(!request.getPassword().equals(request.getConfirmationPassword()))
            throw new PasswordsDontMatchException("Passwords don't match!");

        var user = SakaUser.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        repository.save(user);

        var jwtToken = jwtService.generateToken(user);

        var refreshToken = jwtService.generateRefreshToken(user);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow(()-> new NotFoundException("Username or password is not correct!"));

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (Exception exception) {
            throw new InvalidAuthenticationException("Username or password is not correct!");
        }

        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        authenticationFilter.registerSecurityContextHolder(jwtToken, null);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveUserToken(SakaUser user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(SakaUser user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            throw new InvalidAuthenticationException("Invalid Authentication Info!");
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {

            var user = this.repository.findByEmail(userEmail)
                    .orElseThrow(()-> new NotFoundException("User not found!"));

            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);

                revokeAllUserTokens(user);

                saveUserToken(user, accessToken);

                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    public String getActiveUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return Objects.nonNull(authentication) ? authentication.getName() : "";
    }

    public String getAdminToken() {
        Optional<SakaUser> sakaUser = repository.findByEmail("admin@saka.com");
        SakaUser admin = sakaUser.get();
        return tokenRepository.findAllValidTokenByUser(admin.getId())
                .stream()
                .sorted(Comparator.comparing(Token::getId).reversed())
                .toList().get(0).getToken();
    }
}
