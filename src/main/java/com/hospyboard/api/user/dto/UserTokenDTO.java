package com.hospyboard.api.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserTokenDTO {
    private final String token;
    private final Long expiresInSeconds;
}
