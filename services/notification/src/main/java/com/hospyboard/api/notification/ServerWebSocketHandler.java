package com.hospyboard.api.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hospyboard.api.notification.dto.WebSocketDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.boot.json.JsonParser;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.SubProtocolCapable;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

@Service
public class ServerWebSocketHandler extends TextWebSocketHandler implements SubProtocolCapable {

    private static final Logger logger = LoggerFactory.getLogger(ServerWebSocketHandler.class);
    private static final ResponseMapper responseMapper = new ResponseMapper();

    private static final Set<WebSocketSession> sessions = new CopyOnWriteArraySet<>();
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.info("Server connection opened");
        WebSocketDTO ws = new WebSocketDTO();
        ws.setUserUuid(null);
        ws.setToken(null);
        ws.setSub(null);
        session.getAttributes().put("ws", ws);
        sessions.add(session);
        TextMessage message = new TextMessage("Connected! Authenticate needed to subcribe");
        session.sendMessage(message);
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
        try {
            WebSocketDTO ws = new ObjectMapper().readValue(request, WebSocketDTO.class);
            logger.info("<{}>", ws.getToken());
        } catch (Exception e) {
            session.sendMessage(new TextMessage( responseMapper.generateResponse(false, "Wrong input format").toString()));
        }
        try {
            // check le token
        } catch (Exception e) {
            session.sendMessage(new TextMessage( responseMapper.generateResponse(false, "Wrong token").toString()));
        }

        session.sendMessage(new TextMessage( responseMapper.generateResponse(true, "Authorized and subscription apply").toString()));
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        logger.info("Server transport error: {}", exception.getMessage());
    }

    @Override
    public List<String> getSubProtocols() {
        return Collections.singletonList("subprotocol.demo.websocket");
    }

    public void Notify(Object param) {
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