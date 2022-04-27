package com.hospyboard.api.app.user.repository;

import com.hospyboard.api.app.user.entity.UserToken;
import fr.funixgaming.api.core.crud.repositories.ApiRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTokenRepository extends ApiRepository<UserToken> {
}
