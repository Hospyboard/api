package com.hospyboard.api.user.ressources;

import com.hospyboard.api.user.dto.UserDTO;
import com.hospyboard.api.user.services.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
public class UserResource {

    private final UserService service;

    public UserResource(UserService userService) {
        this.service = userService;
    }

    @RequestMapping
    public String home() {
        return "test ok.";
    }

    @GetMapping("token")
    public String uuid() {
        return this.service.getRandomUUID();
    }

    @PostMapping("login")
    public UserDTO login(@RequestBody UserDTO request) {
        return this.service.login(request);
    }

}
