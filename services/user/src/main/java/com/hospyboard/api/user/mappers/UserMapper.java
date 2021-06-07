package com.hospyboard.api.user.mappers;

import com.hospyboard.api.user.dto.UserDTO;
import com.hospyboard.api.user.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    //@Mapping(target = "uuid", source = "id")
    UserEntity toEntity(UserDTO dto);

    UserDTO toDto(UserEntity entity);
}
