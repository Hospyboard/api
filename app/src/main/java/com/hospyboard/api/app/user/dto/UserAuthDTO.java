package com.hospyboard.api.app.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAuthDTO {
    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
