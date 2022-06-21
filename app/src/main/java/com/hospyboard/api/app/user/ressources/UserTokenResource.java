package com.hospyboard.api.app.user.ressources;

import com.hospyboard.api.app.user.dto.UserTokenDTO;
import com.hospyboard.api.app.user.services.UserTokenService;
import fr.funixgaming.api.core.crud.resources.ApiResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("userToken")
public class UserTokenResource extends ApiResource<UserTokenDTO, UserTokenService> {
    public UserTokenResource(UserTokenService userTokenService) {
        super(userTokenService);
    }
}
