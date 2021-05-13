package com.hospyboard.api.auth;

import com.hospyboard.api.auth.ressources.AuthResource;
import org.springframework.boot.SpringApplication;

public class AuthApp {

    public static void main(String[] args) {
        SpringApplication.run(AuthResource.class, args);
    }

}
