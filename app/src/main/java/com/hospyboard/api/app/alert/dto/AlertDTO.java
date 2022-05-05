package com.hospyboard.api.app.alert.dto;

import com.hospyboard.api.app.alert.enums.AlertImportance;
import com.hospyboard.api.app.alert.enums.AlertStatus;
import com.hospyboard.api.app.alert.enums.AlertType;
import com.hospyboard.api.app.user.dto.UserDTO;
import fr.funixgaming.api.core.crud.dtos.ApiDTO;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class AlertDTO extends ApiDTO {
    private UserDTO patient;

    private UserDTO staff;

    @NotNull
    private AlertType type;

    @NotNull
    private AlertImportance importance;

    @NotNull
    private AlertStatus status;

    private String infos;
}
