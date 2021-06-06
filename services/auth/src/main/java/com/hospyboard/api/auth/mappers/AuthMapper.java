package com.hospyboard.api.auth.mappers;

import com.hospyboard.api.auth.dto.AuthDTO;
import com.hospyboard.api.auth.entity.AuthEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthMapper {
    //@Mapping(target = "uuid", source = "id")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "password", target = "password")
    AuthEntity toEntity(AuthDTO dto);

    @InheritInverseConfiguration
    AuthDTO toDto(AuthEntity entity);
}
