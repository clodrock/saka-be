package com.clodrock.sakabe.model;

import com.clodrock.sakabe.validator.ValidEmail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest {
    @ValidEmail
    private String email;
    private String password;
}
