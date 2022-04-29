package com.hospyboard.api.app.alert.dto;

import com.hospyboard.api.app.alert.enums.AlertImportance;
import com.hospyboard.api.app.alert.enums.AlertStatus;
import com.hospyboard.api.app.alert.enums.AlertType;
import fr.funixgaming.api.core.crud.dtos.ApiDTO;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class AlertDTO extends ApiDTO {
    @NotBlank
    private String patientUuid;

    private String staffUuid;

    @NotBlank
    private AlertType type;

    @NotBlank
    private AlertImportance importance;

    @NotBlank
    private AlertStatus status;
}
