package com.hospyboard.api.user.services;

import com.hospyboard.api.user.dto.AuthDTO;
import com.hospyboard.api.user.entity.AuthEntity;
import com.hospyboard.api.user.mappers.AuthMapper;
import com.hospyboard.api.user.repository.AuthRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthService {

    private final AuthRepository authRepository;
    private final AuthMapper authMapper;

    public AuthService(AuthRepository authRepository,
                       AuthMapper authMapper) {
        this.authRepository = authRepository;
        this.authMapper = authMapper;
    }

    public String getRandomUUID() {
        return UUID.randomUUID().toString();
    }

    public AuthDTO login(final AuthDTO response) {
        AuthEntity authEntity = authRepository.getAuthEntityByUuid(response.getEmail());

        if (authEntity == null) {
            authEntity = new AuthEntity()
                    .setEmail(response.getEmail())
                    .setPassword(response.getPassword());
        }
        return authMapper.toDto(authRepository.save(authEntity));
    }

}
