package com.hospyboard.api.auth.services;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthService {

    public String getRandomUUID() {
        return UUID.randomUUID().toString();
    }

}

