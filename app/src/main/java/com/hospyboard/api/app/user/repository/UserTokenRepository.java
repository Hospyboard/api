package com.hospyboard.api.app.user.repository;

import com.hospyboard.api.app.user.entity.User;
import com.hospyboard.api.app.user.entity.UserToken;
import fr.funixgaming.api.core.crud.repositories.ApiRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface UserTokenRepository extends ApiRepository<UserToken> {
    Set<UserToken> findAllByUser(User user);
    void deleteAllByUser(User user);
}
