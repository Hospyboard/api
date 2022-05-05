package com.hospyboard.api.app.alert.mappers;

import com.hospyboard.api.app.alert.dto.AlertDTO;
import com.hospyboard.api.app.alert.entity.AlertEntity;
import com.hospyboard.api.app.user.mappers.UserMapper;
import fr.funixgaming.api.core.crud.mappers.ApiMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface AlertMapper extends ApiMapper<AlertEntity, AlertDTO> {

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uuid", source = "id")
    AlertEntity toEntity(AlertDTO dto);

    @Override
    @Mapping(target = "id", source = "uuid")
    AlertDTO toDto(AlertEntity entity);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patch(AlertEntity request, @MappingTarget AlertEntity toPatch);
}
