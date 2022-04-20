package com.hospyboard.api.app.log_actions.mappers;

import com.hospyboard.api.app.log_actions.dto.HospyboardActionDTO;
import com.hospyboard.api.app.log_actions.entity.HospyboardAction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ActionMapper {
    @Mapping(target = "uuid", source = "id")
    @Mapping(target = "id", ignore = true)
    HospyboardAction toEntity(HospyboardActionDTO dto);

    HospyboardActionDTO toDto(HospyboardAction hospyboardAction);
}
