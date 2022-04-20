package com.hospyboard.api.app.notification.ressources;

import com.hospyboard.api.app.notification.ServerWebSocketHandler;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("notify")
public class NotificationResource {
    public final ServerWebSocketHandler handler;

    public NotificationResource(ServerWebSocketHandler handler) {this.handler = handler;}

    @PostMapping
    public void notify(@RequestBody Object param) {
        handler.notify(param);

    }

    @GetMapping
    public void notifyGET() {
        handler.notify(null);

    }
}
