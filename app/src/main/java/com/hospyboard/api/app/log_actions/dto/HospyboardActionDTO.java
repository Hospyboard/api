package com.hospyboard.api.app.log_actions.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class HospyboardActionDTO {
    private String id;
    private String userUuid;
    private Instant requestedAt;
    private String service;
    private String routeName;
}
