package com.hospyboard.api.hospital.mappers;

import com.hospyboard.api.hospital.dto.ToolDTO;
import com.hospyboard.api.hospital.entity.Tool;
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
