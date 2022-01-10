package com.hospyboard.api.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.hospyboard.api.notification.dto.SubscriptionDto;
import com.hospyboard.api.notification.dto.WebSocketDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.SubProtocolCapable;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@Service
public class ServerWebSocketHandler extends TextWebSocketHandler implements SubProtocolCapable {

    private static final Logger logger = LoggerFactory.getLogger(ServerWebSocketHandler.class);
    private static final ResponseMapper responseMapper = new ResponseMapper();

    private static final Set<WebSocketSession> sessions = new CopyOnWriteArraySet<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.info("Server connection opened");
        session.getAttributes().put("uuid", "");
        session.getAttributes().put("token", "");
        session.getAttributes().put("alert", false);
        sessions.add(session);
        session.sendMessage(new TextMessage( responseMapper.generateResponse(true, "Connected! Authenticate needed to subscribe").toString()));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        logger.info("Server connection closed: {}", status);
        sessions.remove(session);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String request = message.getPayload();
        logger.info("Server received: {}", request);
        Gson obj = new Gson();
        logger.info(obj.toJson(request));
        int cond = 0;
        if (!session.getAttributes().get("token").toString().isBlank() && !session.getAttributes().get("token").toString().isEmpty()) {
            cond++;
        }
        if (!session.getAttributes().get("uuid").toString().isBlank() && !session.getAttributes().get("uuid").toString().isEmpty()) {
            cond++;
        }
        // connecté => sub
        if (cond == 2) {
            System.out.println("connecté");
            try {
                SubscriptionDto sub = new ObjectMapper().readValue(request, SubscriptionDto.class);
                boolean val = !(boolean)  session.getAttributes().get("alert");
                session.getAttributes().put("alert", val);
                if (val) {
                    session.sendMessage(new TextMessage( responseMapper.generateResponse(true, "Subscription change applied : [alert]").toString()));
                } else {
                    session.sendMessage(new TextMessage( responseMapper.generateResponse(true, "Subscription change applied : no subscription").toString()));
                }

            } catch (Exception e) {
                session.sendMessage(new TextMessage( responseMapper.generateResponse(false, "Subscription: Wrong input format").toString()));
            }

        } else {
            try {
                WebSocketDTO ws = new ObjectMapper().readValue(request, WebSocketDTO.class);
                try {
                    // check le token
                } catch (Exception e) {
                    session.sendMessage(new TextMessage( responseMapper.generateResponse(false, "Wrong token").toString()));
                }
                session.getAttributes().put("uuid", ws.getUserUuid());
                session.getAttributes().put("token", ws.getToken());
                session.sendMessage(new TextMessage( responseMapper.generateResponse(true, "Authorized! waiting for subscription").toString()));
                logger.info("<{}>", ws.getToken());
            } catch (Exception e) {
                session.sendMessage(new TextMessage( responseMapper.generateResponse(false, "Login: Wrong input format").toString()));
                return;
            }
        }

    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        logger.info("Server transport error: {}", exception.getMessage());
    }

    @Override
    public List<String> getSubProtocols() {
        return Collections.singletonList("subprotocol.demo.websocket");
    }

    public void notify(Object param) {
        if (sessions.size() > 0) {
            sessions.forEach(it -> {
                try {
                    if (param != null) {
                        it.sendMessage(new TextMessage(param.toString()));
                    } else {
                        it.sendMessage(new TextMessage("data"));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
