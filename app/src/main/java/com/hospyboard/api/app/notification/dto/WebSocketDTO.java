package com.hospyboard.api.app.notification.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WebSocketDTO {
    private String userUuid;
    private String token;
}
