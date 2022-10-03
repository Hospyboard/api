package com.hospyboard.api.app.log_action.dtos;

import fr.funixgaming.api.core.crud.dtos.ApiDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public class LogActionDTO extends ApiDTO {
    private Date when;
    private String route;
    private String httpMethod;
    private String ip;
    private UUID userUuid;
}
