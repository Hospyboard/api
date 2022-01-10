package com.hospyboard.api.user.ressources;

import com.hospyboard.api.user.dto.UserAuth;
import com.hospyboard.api.user.dto.UserAuthDTO;
import com.hospyboard.api.user.dto.UserDTO;
import com.hospyboard.api.user.services.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.time.Instant;

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

    @PostMapping("login")
    @Transactional
    public UserAuth login(final UserAuthDTO authDTO) {
        final UserAuth userAuth = new UserAuth();

        userAuth.setAuthToken("slkfqs765455678kjsdflksdllmsqd");
        userAuth.setRefreshToken("qkshudqisygd234567890P0000IJHSGFhqdk");
        userAuth.setExpirationDate(Instant.now().plusSeconds(100000));

        return userAuth;
        //TODO impl auth
    }

}
