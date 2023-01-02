package com.hospyboard.api.app.hospital.dto;

import com.hospyboard.api.app.user.dto.UserDTO;
import fr.funixgaming.api.core.crud.dtos.ApiDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RoomDTO extends ApiDTO {

    @NotBlank
    private String name;

    private ServiceDTO service;

    private List<UserDTO> patients;
}
