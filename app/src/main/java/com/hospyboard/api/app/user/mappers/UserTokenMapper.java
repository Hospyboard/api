package com.hospyboard.api.app.user.mappers;

import com.hospyboard.api.app.user.dto.UserTokenDTO;
import com.hospyboard.api.app.user.entity.UserToken;
import fr.funixgaming.api.core.crud.mappers.ApiMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface UserTokenMapper extends ApiMapper<UserToken, UserTokenDTO> {
    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uuid", source = "id")
    UserToken toEntity(UserTokenDTO dto);

    @Override
    @Mapping(target = "id", source = "uuid")
    @Mapping(target = "token", ignore = true)
    UserTokenDTO toDto(UserToken entity);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patch(UserToken request, @MappingTarget UserToken toPatch);
}
