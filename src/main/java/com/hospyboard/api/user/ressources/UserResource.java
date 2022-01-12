package com.hospyboard.api.user.ressources;

import com.hospyboard.api.log_actions.dto.HospyboardActionDTO;
import com.hospyboard.api.log_actions.entity.HospyboardAction;
import com.hospyboard.api.log_actions.services.LogActionService;
import com.hospyboard.api.user.dto.UserCreationDTO;
import com.hospyboard.api.user.dto.UserDTO;
import com.hospyboard.api.user.services.UserService;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.Instant;

@RestController
@RequestMapping("user")
public class UserResource {

    private final UserService service;
    private final LogActionService logActionService;

    public UserResource(UserService userService,
                        LogActionService logActionService) {
        this.service = userService;
        this.logActionService = logActionService;
    }

    @GetMapping("info")
    public UserDTO getActualUser() {
        return service.getActualUser();
    }

    @PostMapping("/register")
    @Transactional
    public UserDTO register(@RequestBody final UserCreationDTO userCreationDTO) {
        final HospyboardActionDTO hospyboardAction = new HospyboardActionDTO();

        hospyboardAction.setRequestedAt(Instant.now());
        hospyboardAction.setUserUuid(null);
        hospyboardAction.setRouteName("/user/register");
        hospyboardAction.setService("UserResource");

        this.logActionService.doAction(hospyboardAction);
        return service.createNewUser(userCreationDTO);
    }

}
