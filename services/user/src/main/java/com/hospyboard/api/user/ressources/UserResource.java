package com.hospyboard.api.user.ressources;

import com.hospyboard.api.user.dto.AuthDTO;
import com.hospyboard.api.user.services.AuthService;
import org.springframework.web.bind.annotation.*;

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
    public AuthDTO login(@RequestBody AuthDTO request) {
        return this.service.login(request);
    }

}
