package com.hospyboard.api.app.notification.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Message {
    private String from;
    private String text;
}