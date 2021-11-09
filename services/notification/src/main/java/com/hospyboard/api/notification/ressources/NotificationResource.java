package com.hospyboard.api.notification.ressources;

import com.hospyboard.api.notification.ServerWebSocketHandler;
import org.springframework.web.bind.annotation.*;

@RestController
public class NotificationResource {
    public final ServerWebSocketHandler handler;

    public NotificationResource(ServerWebSocketHandler handler) {this.handler = handler;}

    @PostMapping("/notify")
    public void Notify(@RequestBody Object param) {
        handler.Notify(param);

    }

    @GetMapping("/notify")
    public void NotifyGET() {
        handler.Notify(null);

    }
}
