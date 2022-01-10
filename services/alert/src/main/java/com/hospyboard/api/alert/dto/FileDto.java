package com.hospyboard.api.alert.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.core.io.ByteArrayResource;

@Getter
@Setter
public class FileDto {
    private String fileName;
    private String en;
    private String fr;
    private String de;
    private String kr;
    private String es;
    private String it;
    private ByteArrayResource content;
}
