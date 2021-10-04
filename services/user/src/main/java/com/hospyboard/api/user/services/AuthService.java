package com.hospyboard.api.user.services;

import com.auth0.jwt.JWT;
import com.hospyboard.api.user.dto.UserRegisterDTO;
import com.hospyboard.api.user.entity.UserEntity;
import com.hospyboard.api.user.exception.RegisterAuthException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService extends UsernamePasswordAuthenticationToken {
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthService(BCryptPasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager) {
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public UserEntity createAccount(final UserRegisterDTO request) throws RegisterAuthException {
        final UserEntity userEntity = new UserEntity();

        if (request.getPassword().equals(request.getPasswordConfirmation())) {
            userEntity.setEmail(request.getEmail());
            userEntity.setPassword(passwordEncoder.encode(request.getPassword()));
        } else {
            throw new RegisterAuthException("The passwords does not match.");
        }

        return userEntity;
    }

    private String getTokenFromUser(final UserEntity userEntity) {
        return JWT.create()
                .withSubject(((UserEntity) authenticationManager.))
    }
}
