package com.hospyboard.api.app.user.dto;

import com.hospyboard.api.app.hospital.dto.RoomDTO;
import fr.funixgaming.api.core.crud.dtos.ApiDTO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserDTO extends ApiDTO {
    @NotBlank
    private String username;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    private String passwordConfirmation;

    private Date lastLoginAt;

    @NotBlank
    private String role;

    private String infos;

    private RoomDTO room;
}
