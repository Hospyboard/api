package com.hospyboard.api.notification.ressources;

import com.hospyboard.api.notification.services.NotificationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("notification")
public class NotificationResource {

    private final NotificationService service;

    public NotificationResource(NotificationService notificationService) {
        this.service = notificationService;
    }


}
