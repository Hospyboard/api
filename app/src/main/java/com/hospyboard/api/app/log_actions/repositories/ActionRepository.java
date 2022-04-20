package com.hospyboard.api.app.log_actions.repositories;

import com.hospyboard.api.app.log_actions.entity.HospyboardAction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActionRepository extends CrudRepository<HospyboardAction, Long> {
}
