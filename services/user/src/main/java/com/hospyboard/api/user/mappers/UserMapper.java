package com.hospyboard.api.user.mappers;

import com.hospyboard.api.user.dto.UserDTO;
import com.hospyboard.api.user.entity.UserEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "uuid", source = "id")
    @Mapping(target = "id", ignore = true)
    UserEntity toEntity(UserDTO dto);

    @InheritInverseConfiguration
    UserDTO toDto(UserEntity entity);
}
