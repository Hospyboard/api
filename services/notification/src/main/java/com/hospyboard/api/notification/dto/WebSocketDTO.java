package com.hospyboard.api.notification.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

@Getter
@Setter
public class WebSocketDTO {
    private String userUuid;
    private String token;
    private Subscription[] sub;
}
