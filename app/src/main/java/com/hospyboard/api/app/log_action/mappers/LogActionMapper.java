package com.hospyboard.api.app.log_action.mappers;

import com.hospyboard.api.app.log_action.dtos.LogActionDTO;
import com.hospyboard.api.app.log_action.entities.LogAction;
import fr.funixgaming.api.core.crud.mappers.ApiMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface LogActionMapper extends ApiMapper<LogAction, LogActionDTO> {

    @Override
    @Mapping(target = "uuid", source = "id")
    @Mapping(target = "id", ignore = true)
    LogAction toEntity(LogActionDTO dto);

    @Override
    @Mapping(target = "id", source = "uuid")
    LogActionDTO toDto(LogAction entity);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patch(LogAction request, @MappingTarget LogAction toPatch);
}
