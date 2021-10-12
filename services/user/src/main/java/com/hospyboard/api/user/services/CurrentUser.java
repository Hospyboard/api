package com.hospyboard.api.user.services;

import com.hospyboard.api.user.dto.UserDTO;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.representations.AccessToken;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
public class CurrentUser {

    @Nullable
    public UserDTO getCurrentUser() {
        final SecurityContext securityContext = SecurityContextHolder.getContext();
        final Authentication authentication = securityContext.getAuthentication();

        if (authentication == null) {
            return null;
        }

        final Object principal = authentication.getPrincipal();

        if (principal instanceof KeycloakPrincipal) {
            final KeycloakPrincipal keycloakPrincipal = (KeycloakPrincipal) principal;
            AccessToken accessToken = ((KeycloakPrincipal<?>) principal).getKeycloakSecurityContext().getToken();
            return new UserDTO()
                    .setEmail(accessToken.getEmail())
                    .setId(accessToken.getId())
                    .setToken(accessToken.getAccessTokenHash());
        } else {
            return null;
        }
    }

}
