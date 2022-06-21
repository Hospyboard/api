package com.hospyboard.api.app.hospital.dto;

import fr.funixgaming.api.core.crud.dtos.ApiDTO;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
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
