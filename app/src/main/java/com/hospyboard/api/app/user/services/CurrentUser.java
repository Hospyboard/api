package com.hospyboard.api.app.user.services;

import com.hospyboard.api.app.user.dto.UserDTO;
import com.hospyboard.api.app.user.entity.User;
import com.hospyboard.api.app.user.mappers.UserMapper;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUser {

    private final UserMapper userMapper;

    public CurrentUser(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Nullable
    public UserDTO getCurrentUser() {
        final SecurityContext securityContext = SecurityContextHolder.getContext();
        final Authentication authentication = securityContext.getAuthentication();

        if (authentication == null) {
            return null;
        }

        final Object principal = authentication.getPrincipal();

        if (principal instanceof User) {
            return this.userMapper.toDto((User) principal);
        }

        return null;
    }

}
