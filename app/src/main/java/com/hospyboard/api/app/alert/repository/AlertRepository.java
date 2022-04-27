package com.hospyboard.api.app.alert.repository;

import com.hospyboard.api.app.alert.entity.AlertEntity;
import fr.funixgaming.api.core.crud.repositories.ApiRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlertRepository extends ApiRepository<AlertEntity> {
}
