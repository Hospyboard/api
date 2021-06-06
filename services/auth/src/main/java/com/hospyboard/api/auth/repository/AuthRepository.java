package com.hospyboard.api.auth.repository;

import com.hospyboard.api.auth.entity.AuthEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepository extends CrudRepository<AuthEntity, Long> {
    AuthEntity getAuthEntityByUuid(final String uuid);
    AuthEntity getAuthEntityByEmail(final String email);
}