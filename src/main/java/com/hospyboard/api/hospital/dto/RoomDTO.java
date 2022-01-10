package com.hospyboard.api.hospital.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class RoomDTO {
    private String id;
    private String roomNumber;
    private Set<ToolDTO> tools;
    private Set<String> patients;
}
