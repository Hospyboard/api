package com.hospyboard.api.app.alert.mappers;

import com.hospyboard.api.app.alert.dto.AlertDTO;
import com.hospyboard.api.app.alert.entity.AlertEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AlertMapper {
    @Mapping(target = "id", ignore = true)
    AlertEntity toEntity(AlertDTO dto);

    AlertDTO toDto(AlertEntity entity);
}
