package com.clodrock.sakabe.service;

import com.clodrock.sakabe.entity.SakaUser;
import com.clodrock.sakabe.exception.InvalidAuthenticationException;
import com.clodrock.sakabe.model.ChangePasswordRequest;
import com.clodrock.sakabe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;

    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {
        var user = (SakaUser) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new InvalidAuthenticationException("Wrong password!");
        }
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new InvalidAuthenticationException("Password are not the same!");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        repository.save(user);
    }

    public List<SakaUser> findUsersByEmailList(List<String> emailList){
        return repository.findByEmailIn(emailList);
    }
}
