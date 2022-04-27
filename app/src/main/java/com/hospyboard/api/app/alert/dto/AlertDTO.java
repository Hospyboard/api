package com.hospyboard.api.app.alert.dto;

import com.hospyboard.api.app.alert.enums.AlertImportance;
import com.hospyboard.api.app.alert.enums.AlertStatus;
import com.hospyboard.api.app.alert.enums.AlertType;
import fr.funixgaming.api.core.crud.dtos.ApiDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlertDTO extends ApiDTO {
    private String patientUuid;
    private String staffUuid;
    private AlertType type;
    private AlertImportance importance;
    private AlertStatus status;
}
