package com.hospyboard.api.app.user.repository;

import com.hospyboard.api.app.user.entity.User;
import com.hospyboard.api.app.user.entity.UserPasswordReset;
import fr.funixgaming.api.core.crud.repositories.ApiRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserForgotPasswordResetRepository extends ApiRepository<UserPasswordReset> {
    Optional<UserPasswordReset> findUserPasswordResetByCode(String code);

    Optional<UserPasswordReset> findUserPasswordResetByUser(User user);

    void deleteAllByUser(User user);
}
