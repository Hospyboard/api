package com.hospyboard.api.user.repository;

import com.hospyboard.api.user.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {
    UserEntity getAuthEntityByUuid(final String uuid);
    UserEntity getAuthEntityByEmail(final String email);
}
