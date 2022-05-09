package com.hospyboard.api.app.alert.repository;

import com.hospyboard.api.app.alert.entity.AlertEntity;
import com.hospyboard.api.app.user.entity.User;
import fr.funixgaming.api.core.crud.repositories.ApiRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface AlertRepository extends ApiRepository<AlertEntity> {
    void deleteAllByPatient(User patient);
    void deleteAllByStaff(User staff);
}
