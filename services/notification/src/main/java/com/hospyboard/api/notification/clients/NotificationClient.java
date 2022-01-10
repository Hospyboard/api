package com.hospyboard.api.notification.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@FeignClient(name = "notification", url = "${feign.url}")
@RestController
public interface NotificationClient {

    @PostMapping("/notify")
    void notify(@RequestBody Object param);

    @GetMapping("/notify")
    void notifyGET();

}
