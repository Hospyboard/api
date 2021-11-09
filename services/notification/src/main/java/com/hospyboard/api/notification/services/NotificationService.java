package com.hospyboard.api.notification.services;

import com.hospyboard.api.notification.dto.Subscription;
import com.hospyboard.api.notification.dto.WebSocketDTO;
import org.springframework.stereotype.Service;
import java.util.Hashtable;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.server.ServerEndpointConfig;

@Service
public class NotificationService {
    private Hashtable<String, Session> webSocketPool = new Hashtable<>();

    @OnOpen
    public void open(Session session, String userUuid, String token, Subscription[] subList) {
        WebSocketDTO ws = new WebSocketDTO();
        ws.setUserUuid(userUuid);
        ws.setToken(token);
        ws.setSub(subList);
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
        ws.setSub(subList);
        session.getUserProperties().put( "ws", ws );
    }
}
