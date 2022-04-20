package com.hospyboard.api.app.notification.services;

import com.hospyboard.api.app.notification.dto.Subscription;
import com.hospyboard.api.app.notification.dto.WebSocketDTO;
import org.springframework.stereotype.Service;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import java.util.Hashtable;

@Service
public class NotificationService {
    private Hashtable<String, Session> webSocketPool = new Hashtable<>();

    @OnOpen
    public void open(Session session, String userUuid, String token, Subscription[] subList) {
        WebSocketDTO ws = new WebSocketDTO();
        ws.setUserUuid(userUuid);
        ws.setToken(token);
        session.getUserProperties().put( "ws", ws );
        webSocketPool.put(session.getId(), session);
    }

    @OnClose
    public void close(Session session) {
        webSocketPool.remove( session.getId() );
    }

    @OnMessage
    public void handleMessage(Subscription[] subList, Session session) {
        WebSocketDTO ws = (WebSocketDTO) session.getUserProperties().get("ws");

        session.getUserProperties().put( "ws", ws );
    }
}
