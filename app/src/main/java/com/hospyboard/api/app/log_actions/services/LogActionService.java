package com.hospyboard.api.app.log_actions.services;

import com.hospyboard.api.app.log_actions.dto.HospyboardActionDTO;
import com.hospyboard.api.app.log_actions.entity.HospyboardAction;
import com.hospyboard.api.app.log_actions.mappers.ActionMapper;
import com.hospyboard.api.app.log_actions.repositories.ActionRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LogActionService {

    private final ActionRepository actionRepository;
    private final ActionMapper actionMapper;

    public LogActionService(ActionRepository actionRepository,
                            ActionMapper actionMapper) {
        this.actionRepository = actionRepository;
        this.actionMapper = actionMapper;
    }

    public List<HospyboardActionDTO> getAllActions() {
        final List<HospyboardActionDTO> actions = new ArrayList<>();

        for (final HospyboardAction hospyboardAction : this.actionRepository.findAll()) {
            actions.add(this.actionMapper.toDto(hospyboardAction));
        }
        return actions;
    }

    public HospyboardActionDTO doAction(final HospyboardActionDTO action) {
        final HospyboardAction actionEntity = this.actionMapper.toEntity(action);
        return this.actionMapper.toDto(this.actionRepository.save(actionEntity));
    }

}
