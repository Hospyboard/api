package com.hospyboard.api.user.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UserAuthDTO {

    @NotNull
    private String username;

    @NotNull
    private String password;
}
