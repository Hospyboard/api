package com.hospyboard.api.app.alert.dto;

import com.hospyboard.api.app.alert.enums.AlertImportance;
import com.hospyboard.api.app.alert.enums.AlertStatus;
import com.hospyboard.api.app.alert.enums.AlertType;
import fr.funixgaming.api.core.crud.dtos.ApiDTO;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class AlertDTO extends ApiDTO {
    @NotBlank
    private String patientUuid;

    private String staffUuid;

    @NotNull
    private AlertType type;

    @NotNull
    private AlertImportance importance;

    @NotNull
    private AlertStatus status;
}
