package com.hospyboard.api.auth.services;

import com.hospyboard.api.auth.dto.AuthDTO;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthService {

    public String getRandomUUID() {
        return UUID.randomUUID().toString();
    }

    public AuthDTO login(final AuthDTO response) {

    }

}
