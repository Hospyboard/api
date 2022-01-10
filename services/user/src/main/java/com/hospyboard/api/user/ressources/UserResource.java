package com.hospyboard.api.user.ressources;

import com.hospyboard.api.user.clients.UserClient;
import com.hospyboard.api.user.dto.UserAuthDTO;
import com.hospyboard.api.user.dto.UserDTO;
import com.hospyboard.api.user.services.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class UserResource implements UserClient {

    private final UserService service;

    public UserResource(UserService userService) {
        this.service = userService;
    }
    
    @Override
    public UserDTO getActualUser() {
        return service.getActualUser();
    }

    @Override
    public UserDTO login(final UserAuthDTO authDTO) {
        return null;
        //TODO impl auth
    }

}
