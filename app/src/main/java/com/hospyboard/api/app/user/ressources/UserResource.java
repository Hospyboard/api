package com.hospyboard.api.app.user.ressources;

import com.hospyboard.api.app.user.config.JwtTokenUtil;
import com.hospyboard.api.app.user.dto.*;
import com.hospyboard.api.app.user.entity.User;
import com.hospyboard.api.app.user.exception.LoginHospyboardException;
import com.hospyboard.api.app.user.services.CurrentUser;
import com.hospyboard.api.app.user.services.UserService;
import fr.funixgaming.api.core.crud.resources.ApiResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("user")
public class UserResource extends ApiResource<UserDTO, UserService> {

    private final CurrentUser currentUser;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    public UserResource(UserService userService,
                        AuthenticationManager authenticationManager,
                        CurrentUser currentUser,
                        JwtTokenUtil jwtTokenUtil) {
        super(userService);
        this.currentUser = currentUser;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @GetMapping("session")
    public UserDTO getActualUser() {
        return currentUser.getCurrentUser();
    }

    @GetMapping("logout")
    public void logoutUser() {
        this.jwtTokenUtil.invalidateTokens(currentUser.getCurrentUser().getId());
    }

    @PatchMapping("changePassword")
    public void changePassword(@RequestBody final UserResetPasswordDTO request) {
        getService().changePassword(request);
    }

    @PostMapping("forgotPassword")
    public void forgotPassword(@RequestBody final UserForgotPasswordDTO request) {
        request.setPasswordSet(false);
        getService().resetPassword(request);
    }

    @PostMapping("forgotPassword/change")
    public void changePassword(@RequestBody final UserForgotPasswordDTO request) {
        request.setPasswordSet(true);
        getService().resetPassword(request);
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
        return getService().register(userCreationDTO);
    }

}
