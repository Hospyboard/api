package com.hospyboard.api.notification.ressources;

import com.hospyboard.api.notification.ServerWebSocketHandler;
import com.hospyboard.api.notification.clients.NotificationClient;
import org.springframework.web.bind.annotation.*;

public class NotificationResource implements NotificationClient {
    public final ServerWebSocketHandler handler;

    public NotificationResource(ServerWebSocketHandler handler) {this.handler = handler;}

    @Override
    public void notify(@RequestBody Object param) {
        handler.notify(param);

    }

    @Override
    public void notifyGET() {
        handler.notify(null);

    }
}
