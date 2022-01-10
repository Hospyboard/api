package com.hospyboard.api.log_actions.clients;

import com.hospyboard.api.log_actions.dto.HospyboardActionDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.transaction.Transactional;

@FeignClient(name = "log-actions", url = "${feign.url}")
public interface HospyboardActionsClient {

    @PostMapping
    @Transactional
    HospyboardActionDTO doAction(@RequestBody HospyboardActionDTO actionDTO);

}
