package com.hospyboard.api.user.repository;

import com.hospyboard.api.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User getUserByUuid(final String uuid);
    User getUserByEmail(final String email);
}
