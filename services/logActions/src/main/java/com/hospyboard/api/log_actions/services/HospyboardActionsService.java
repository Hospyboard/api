package com.hospyboard.api.log_actions.services;

import com.hospyboard.api.log_actions.dto.HospyboardActionDTO;
import com.hospyboard.api.log_actions.entity.HospyboardAction;
import com.hospyboard.api.log_actions.mappers.ActionMapper;
import com.hospyboard.api.log_actions.repositories.ActionRepository;
import org.springframework.stereotype.Service;

@Service
public class HospyboardActionsService {

    private final ActionRepository actionRepository;
    private final ActionMapper actionMapper;

    public HospyboardActionsService(ActionRepository actionRepository,
                                    ActionMapper actionMapper) {
        this.actionRepository = actionRepository;
        this.actionMapper = actionMapper;
    }

    public HospyboardActionDTO doAction(final HospyboardActionDTO action) {
        final HospyboardAction actionEntity = this.actionMapper.toEntity(action);
        return this.actionMapper.toDto(this.actionRepository.save(actionEntity));
    }

}
