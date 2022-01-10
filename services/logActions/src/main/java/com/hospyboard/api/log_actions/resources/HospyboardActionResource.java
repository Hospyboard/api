package com.hospyboard.api.log_actions.resources;

import com.hospyboard.api.log_actions.clients.HospyboardActionsClient;
import com.hospyboard.api.log_actions.dto.HospyboardActionDTO;
import com.hospyboard.api.log_actions.services.HospyboardActionsService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class HospyboardActionResource implements HospyboardActionsClient {

    private final HospyboardActionsService actionsService;

    public HospyboardActionResource(HospyboardActionsService actionsService) {
        this.actionsService = actionsService;
    }

    @Override
    public HospyboardActionDTO doAction(@RequestBody HospyboardActionDTO actionDTO) {
        return this.actionsService.doAction(actionDTO);
    }

}
