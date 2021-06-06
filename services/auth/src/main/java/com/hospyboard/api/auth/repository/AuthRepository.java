package com.hospyboard.api.auth.repository;

import com.hospyboard.api.auth.entity.AuthEntity;
import org.springframework.data.repository.Repository;

public interface AuthRepository extends Repository<AuthEntity, Long> {
}
