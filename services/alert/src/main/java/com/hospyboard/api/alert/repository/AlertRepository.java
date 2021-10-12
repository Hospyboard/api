package com.hospyboard.api.alert.repository;

import com.hospyboard.api.alert.entity.AlertEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlertRepository extends CrudRepository<AlertEntity, Long> {
    AlertEntity getAlertEntityByAlertUuid(final String uuid);
}
