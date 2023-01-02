package com.hospyboard.api.app.hospital.dto;

import fr.funixgaming.api.core.crud.dtos.ApiDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class HospitalDTO extends ApiDTO {

    @NotBlank
    private String name;

    @NotBlank
    private String address;

    private Set<ServiceDTO> services;
}
