package com.hospyboard.api.hospital.dto;

import com.hospyboard.api.hospital.enums.ToolTypeEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ToolDTO {
    private String id;
    private ToolTypeEnum toolType;
}
