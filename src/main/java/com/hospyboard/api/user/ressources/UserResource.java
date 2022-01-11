package com.hospyboard.api.user.ressources;

import com.hospyboard.api.user.dto.UserAuthDTO;
import com.hospyboard.api.user.dto.UserCreationDTO;
import com.hospyboard.api.user.dto.UserDTO;
import com.hospyboard.api.user.services.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;

@RestController
@RequestMapping("user")
public class UserResource {

    private final UserService service;

    public UserResource(UserService userService) {
        this.service = userService;
    }

    @GetMapping
    public UserDTO getActualUser() {
        return service.getActualUser();
    }

    @PostMapping("register")
    @Transactional
    public UserDTO register(final UserCreationDTO userCreationDTO) {
        return service.createNewUser(userCreationDTO);
    }

}
