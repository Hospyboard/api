package com.hospyboard.api.user.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class UserAuth {
    private String authToken;
    private String refreshToken;
    private Instant expirationDate;
}
