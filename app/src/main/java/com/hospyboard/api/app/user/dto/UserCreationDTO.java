package com.hospyboard.api.app.user.dto;

import fr.funixgaming.api.core.crud.dtos.ApiDTO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreationDTO extends ApiDTO {
    @NotBlank
    private String username;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank @Email
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String passwordConfirmation;
    private String infos;
}
