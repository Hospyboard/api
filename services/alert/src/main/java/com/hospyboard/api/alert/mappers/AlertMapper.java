package com.hospyboard.api.alert.mappers;

import com.hospyboard.api.alert.dto.AlertDTO;
import com.hospyboard.api.alert.entity.AlertEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AlertMapper {
<<<<<<< Updated upstream
    @Mapping(target = "id", ignore = true)
=======

>>>>>>> Stashed changes
    AlertEntity toEntity(AlertDTO dto);

    AlertDTO toDto(AlertEntity entity);
}
