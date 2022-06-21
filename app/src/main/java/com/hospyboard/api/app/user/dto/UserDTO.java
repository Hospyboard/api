package com.hospyboard.api.app.user.dto;

import com.hospyboard.api.app.hospital.dto.RoomDTO;
import fr.funixgaming.api.core.crud.dtos.ApiDTO;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.UUID;

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

    @NotBlank
    private String role;

    private String infos;

    private RoomDTO room;
}
