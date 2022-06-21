package com.hospyboard.api.app.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserForgotPasswordDTO {
    private String email;
    private String code;
    private String password;
    private String passwordConfirmation;

    private boolean passwordSet = false;
}
