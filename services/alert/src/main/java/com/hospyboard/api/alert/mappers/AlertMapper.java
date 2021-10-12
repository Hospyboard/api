package com.hospyboard.api.alert.mappers;

import com.hospyboard.api.alert.dto.AlertDTO;
import com.hospyboard.api.alert.entity.AlertEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AlertMapper {
    @Mapping(target = "id", ignore = true)
    AlertEntity toEntity(AlertDTO dto);

    AlertDTO toDto(AlertEntity entity);
}
