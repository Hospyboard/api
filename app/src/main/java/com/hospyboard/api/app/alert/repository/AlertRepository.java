package com.hospyboard.api.app.alert.repository;

import com.hospyboard.api.app.alert.entity.AlertEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlertRepository extends CrudRepository<AlertEntity, Long> {
    AlertEntity getAlertEntityByAlertUuid(final String uuid);
}
