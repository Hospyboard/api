package com.hospyboard.api.app.user.mappers;

import com.hospyboard.api.app.user.dto.UserCreationDTO;
import com.hospyboard.api.app.user.dto.UserDTO;
import com.hospyboard.api.app.user.entity.User;
import com.hospyboard.api.app.user.enums.UserRole;
import fr.funixgaming.api.core.crud.mappers.ApiMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper extends ApiMapper<User, UserDTO> {
    @Override
    @Mapping(target = "uuid", source = "id")
    @Mapping(target = "id", ignore = true)
    User toEntity(UserDTO dto);

    @Override
    @Mapping(target = "id", source = "uuid")
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "passwordConfirmation", ignore = true)
    UserDTO toDto(User entity);

    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", constant = UserRole.PATIENT)
    User fromUserCreationToEntity(UserCreationDTO userCreationDTO);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "authorities", ignore = true)
    void patch(User newUser, @MappingTarget User oldUser);
}
