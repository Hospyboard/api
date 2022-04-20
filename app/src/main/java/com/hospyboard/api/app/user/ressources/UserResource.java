package com.hospyboard.api.app.user.ressources;

import com.hospyboard.api.app.user.config.JwtTokenUtil;
import com.hospyboard.api.app.user.dto.UserAuthDTO;
import com.hospyboard.api.app.user.dto.UserCreationDTO;
import com.hospyboard.api.app.user.dto.UserDTO;
import com.hospyboard.api.app.user.dto.UserTokenDTO;
import com.hospyboard.api.app.user.entity.User;
import com.hospyboard.api.app.user.exception.LoginHospyboardException;
import com.hospyboard.api.app.user.services.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("user")
public class UserResource {

    private final UserService service;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    public UserResource(UserService userService,
                        AuthenticationManager authenticationManager,
                        JwtTokenUtil jwtTokenUtil) {
        this.service = userService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @GetMapping
    public UserDTO getActualUser() {
        return service.getActualUser();
    }

    @PatchMapping
    public UserDTO partialUpdateUser(@RequestBody final UserDTO user) {
        return this.service.updateUser(user);
    }

    @PutMapping
    public UserDTO updateUser(@RequestBody @Valid final UserDTO user) {
        return this.service.updateUser(user);
    }

    @PostMapping("login")
    public UserTokenDTO login(@RequestBody @Valid final UserAuthDTO request) {
        try {
            Authentication authenticate = authenticationManager
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    request.getUsername(), request.getPassword()
                            )
                    );

            User user = (User) authenticate.getPrincipal();

            return jwtTokenUtil.generateAccessToken(user);
        } catch (BadCredentialsException ex) {
            throw new LoginHospyboardException(ex);
        }
    }

    @PostMapping("register")
    public UserDTO register(@RequestBody @Valid final UserCreationDTO userCreationDTO) {
        return service.createNewUser(userCreationDTO);
    }

}
