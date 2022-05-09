package com.hospyboard.api.app.log_action.repositories;

import com.hospyboard.api.app.log_action.entities.LogAction;
import fr.funixgaming.api.core.crud.repositories.ApiRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogActionRepository extends ApiRepository<LogAction> {
}
