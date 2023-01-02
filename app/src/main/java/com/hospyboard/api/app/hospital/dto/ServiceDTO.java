package com.hospyboard.api.app.hospital.dto;

import fr.funixgaming.api.core.crud.dtos.ApiDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class ServiceDTO extends ApiDTO {

    @NotBlank
    private String name;

    private HospitalDTO hospital;

    private Set<RoomDTO> rooms;
}
