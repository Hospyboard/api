package com.hospyboard.api.app.hospital.dto;

import com.hospyboard.api.app.hospital.enums.ToolTypeEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ToolDTO {
    private String id;
    private ToolTypeEnum toolType;
}
