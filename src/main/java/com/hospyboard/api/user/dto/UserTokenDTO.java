package com.hospyboard.api.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@Getter
@AllArgsConstructor
public class UserTokenDTO {
    private final String token;
    private final Instant expiresAt;
}
