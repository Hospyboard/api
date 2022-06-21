package com.hospyboard.api.app.hospital.dto;

import fr.funixgaming.api.core.crud.dtos.ApiDTO;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Getter
@Setter
public class ServiceDTO extends ApiDTO {

    @NotBlank
    private String name;

    private HospitalDTO hospital;

    private Set<RoomDTO> rooms;
}
