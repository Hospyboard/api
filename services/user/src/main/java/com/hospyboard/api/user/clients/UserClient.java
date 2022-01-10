package com.hospyboard.api.user.clients;

import com.hospyboard.api.user.dto.UserAuth;
import com.hospyboard.api.user.dto.UserAuthDTO;
import com.hospyboard.api.user.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.transaction.Transactional;

@FeignClient(name = "user", url = "${feign.url}")
public interface UserClient {
    @GetMapping
    UserDTO getActualUser();

    @PostMapping("login")
    @Transactional
    UserAuth login(final UserAuthDTO authDTO);
}
