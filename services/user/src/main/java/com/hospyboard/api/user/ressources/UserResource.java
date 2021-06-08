package com.hospyboard.api.user.ressources;

import com.hospyboard.api.user.dto.UserDTO;
import com.hospyboard.api.user.dto.UserLoginDTO;
import com.hospyboard.api.user.dto.UserRegisterDTO;
import com.hospyboard.api.user.services.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
public class UserResource {

    private final UserService service;

    public UserResource(UserService userService) {
        this.service = userService;
    }

    @PostMapping("register")
    public UserDTO register(@RequestBody UserRegisterDTO request) throws Exception {
        return this.service.register(request);
    }

    @PostMapping("login")
    public UserDTO login(@RequestBody UserLoginDTO request) throws Exception {
        return this.service.login(request);
    }

}
