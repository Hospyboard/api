package com.hospyboard.api.app.user.repository;

import com.hospyboard.api.app.user.entity.User;
import fr.funixgaming.api.core.crud.repositories.ApiRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends ApiRepository<User> {
    Optional<User> findByUsername(String username);
    Iterable<User> findAllByEmail(String email);
    Iterable<User> findAllByRoomUuid(String roomUuid);
}
