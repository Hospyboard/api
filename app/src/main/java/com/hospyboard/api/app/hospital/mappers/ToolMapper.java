package com.hospyboard.api.app.hospital.mappers;

import com.hospyboard.api.app.hospital.dto.ToolDTO;
import com.hospyboard.api.app.hospital.entity.Tool;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ToolMapper {
    @Mapping(target = "uuid", source = "id")
    @Mapping(target = "id", ignore = true)
    Tool toEntity(ToolDTO dto);

    @InheritConfiguration
    ToolDTO toDto(Tool tool);
}