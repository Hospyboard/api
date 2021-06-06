package com.hospyboard.api.auth.mappers;

import com.hospyboard.api.auth.dto.AuthDTO;
import com.hospyboard.api.auth.entity.AuthEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface AuthMapper {
    @Mapping(target = "uuid", source = "id")
    AuthEntity toEntity(AuthDTO dto);

    @InheritInverseConfiguration
    AuthDTO toDto(AuthEntity entity);
}
