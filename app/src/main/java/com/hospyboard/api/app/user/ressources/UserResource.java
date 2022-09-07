package com.hospyboard.api.app.user.ressources;

import com.hospyboard.api.app.user.config.JwtTokenUtil;
import com.hospyboard.api.app.user.dto.*;
import com.hospyboard.api.app.user.entity.User;
import com.hospyboard.api.app.user.enums.UserRole;
import com.hospyboard.api.app.user.exception.LoginHospyboardException;
import com.hospyboard.api.app.user.services.CurrentUser;
import com.hospyboard.api.app.user.services.UserService;
import fr.funixgaming.api.core.exceptions.ApiForbiddenException;
import fr.funixgaming.api.core.exceptions.ApiNotFoundException;
import org.springframework.data.domain.Page;
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
    private final CurrentUser currentUser;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    public UserResource(UserService userService,
                        AuthenticationManager authenticationManager,
                        CurrentUser currentUser,
                        JwtTokenUtil jwtTokenUtil) {
        this.service = userService;
        this.currentUser = currentUser;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @GetMapping("session")
    public UserDTO getActualUser() {
        return currentUser.getCurrentUser();
    }

    @GetMapping("{id}")
    public UserDTO getUserById(@PathVariable String id) {
        final UserDTO userDTO = service.findById(id);

        if (userDTO == null) {
            throw new ApiNotFoundException("Utilisateur non trouvé.");
        } else {
            return userDTO;
        }
    }

    @GetMapping("logout")
    public void logoutUser() {
        this.jwtTokenUtil.invalidateTokens(currentUser.getCurrentUser().getId());
    }

    @GetMapping
    public Page<UserDTO> getAllUsers(@RequestParam(value = "page", defaultValue = "0") String page,
                                     @RequestParam(value = "elemsPerPage", defaultValue = "300") String elemsPerPage,
                                     @RequestParam(value = "search", defaultValue = "") String search,
                                     @RequestParam(value = "sort", defaultValue = "") String sort) {
        return this.service.getAll(page, elemsPerPage, search, sort);
    }

    @PatchMapping
    public UserDTO patchUser(@RequestBody final UserDTO user) {
        return this.service.updateUser(user);
    }

    @PatchMapping("changePassword")
    public void changePassword(@RequestBody final UserResetPasswordDTO request) {
        this.service.changePassword(request);
    }

    @PostMapping("forgotPassword")
    public void forgotPassword(@RequestBody final UserForgotPasswordDTO request) {
        request.setPasswordSet(false);
        this.service.resetPassword(request);
    }

    @PostMapping("forgotPassword/change")
    public void changePassword(@RequestBody final UserForgotPasswordDTO request) {
        request.setPasswordSet(true);
        this.service.resetPassword(request);
    }

    @PostMapping
    public UserDTO createUser(@RequestBody @Valid final UserDTO user) {
        final UserDTO currentUser = this.currentUser.getCurrentUser();

        if (currentUser.getRole().equals(UserRole.ADMIN)) {
            return this.service.create(user);
        } else {
            throw new ApiForbiddenException("Vous n'êtes pas un admin.");
        }
    }

    @DeleteMapping
    public void deleteUser(@RequestParam("id") String id) {
        if (this.currentUser.getCurrentUser().getRole().equals(UserRole.ADMIN)) {
            this.service.delete(id);
        } else {
            throw new ApiForbiddenException("Vous n'êtes pas un admin.");
        }
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
        return service.register(userCreationDTO);
    }

}
