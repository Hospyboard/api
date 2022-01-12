package com.hospyboard.api.user.ressources;

import com.hospyboard.api.user.config.JwtTokenUtil;
import com.hospyboard.api.user.dto.UserAuthDTO;
import com.hospyboard.api.user.dto.UserCreationDTO;
import com.hospyboard.api.user.dto.UserDTO;
import com.hospyboard.api.user.dto.UserTokenDTO;
import com.hospyboard.api.user.entity.User;
import com.hospyboard.api.user.exception.LoginHospyboardException;
import com.hospyboard.api.user.services.UserService;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/login")
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

    @PostMapping("/register")
    public UserDTO register(@RequestBody final UserCreationDTO userCreationDTO) {
        return service.createNewUser(userCreationDTO);
    }

}
