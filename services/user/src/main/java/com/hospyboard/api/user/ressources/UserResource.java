package com.hospyboard.api.user.ressources;

import com.hospyboard.api.user.dto.UserDTO;
import com.hospyboard.api.user.services.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class UserResource {

    private final UserService service;

    public UserResource(UserService userService) {
        this.service = userService;
    }

    @GetMapping
    public UserDTO getActualUser() {
        return service.getActualUser();
    }

    @PostMapping("login")
    public UserDTO login() {

    }

}
