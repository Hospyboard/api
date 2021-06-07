package com.hospyboard.api.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthDTO {
    private String id;
    private String email;
    private String password;
}
