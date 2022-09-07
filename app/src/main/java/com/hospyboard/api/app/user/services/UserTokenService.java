package com.hospyboard.api.app.user.services;

import com.hospyboard.api.app.user.dto.UserTokenDTO;
import com.hospyboard.api.app.user.entity.UserToken;
import com.hospyboard.api.app.user.mappers.UserTokenMapper;
import com.hospyboard.api.app.user.repository.UserTokenRepository;
import fr.funixgaming.api.core.crud.services.ApiService;
import fr.funixgaming.api.core.exceptions.ApiBadRequestException;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserTokenService extends ApiService<UserTokenDTO, UserToken, UserTokenMapper, UserTokenRepository> {

    public UserTokenService(UserTokenRepository repository, UserTokenMapper mapper) {
        super(repository, mapper);
    }

    @NotNull
    @Override
    public UserTokenDTO create(UserTokenDTO request) {
        throw new ApiBadRequestException("Route inaccessible.");
    }

    @NotNull
    @Override
    public UserTokenDTO update(UserTokenDTO request) {
        throw new ApiBadRequestException("Route inaccessible.");
    }

    @Override
    public List<UserTokenDTO> update(List<UserTokenDTO> request) {
        throw new ApiBadRequestException("Route inaccessible.");
    }
}
