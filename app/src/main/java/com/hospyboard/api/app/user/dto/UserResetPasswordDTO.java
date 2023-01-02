package com.hospyboard.api.app.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResetPasswordDTO {
    @NotBlank
    private String newPassword;

    @NotBlank
    private String newPasswordConfirmation;
}
