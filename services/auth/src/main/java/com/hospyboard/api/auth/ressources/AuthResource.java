package com.hospyboard.api.auth.ressources;

import com.hospyboard.api.auth.dto.AuthDTO;
import com.hospyboard.api.auth.services.AuthService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthResource {

    private final AuthService service;

    public AuthResource(AuthService authService) {
        this.service = authService;
    }

    @RequestMapping
    public String home() {
        return "test ok.";
    }

    @GetMapping("/token")
    public String uuid() {
        return this.service.getRandomUUID();
    }

    @PostMapping("/auth/login")
    public AuthDTO login(final AuthDTO request) {
        return this.service.login(request);
    }

}
