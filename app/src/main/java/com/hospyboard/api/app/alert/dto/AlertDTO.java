package com.hospyboard.api.app.alert.dto;

import com.hospyboard.api.app.alert.enums.AlertImportance;
import com.hospyboard.api.app.alert.enums.AlertStatus;
import com.hospyboard.api.app.alert.enums.AlertType;
import com.hospyboard.api.app.user.dto.UserDTO;
import fr.funixgaming.api.core.crud.dtos.ApiDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlertDTO extends ApiDTO {
    private UserDTO patient;

    private UserDTO staff;

    @NotNull
    private AlertType type;

    @NotNull
    private AlertImportance importance;

    private AlertStatus status = AlertStatus.PENDING;

    private String infos;
}
