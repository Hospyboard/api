package com.hospyboard.api.alert.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlertDTO {
    private String alertUuid;
    private String patientUuid;
    private AlertType type;
    private AlertImportance importance;
    private AlertStatus status;
    private String staffUuid;
}

