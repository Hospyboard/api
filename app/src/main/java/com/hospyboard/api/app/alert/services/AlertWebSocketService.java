package com.hospyboard.api.app.alert.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospyboard.api.app.alert.dto.AlertDTO;
import fr.funixgaming.api.core.exceptions.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class AlertWebSocketService extends TextWebSocketHandler {

    private final List<WebSocketSession> sessions = new ArrayList<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) {
        if (session.isOpen()) {
            sessions.add(session);
        }
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session,
                                      @NonNull CloseStatus status) {
        for (final WebSocketSession webSocketSession : sessions) {
            if (webSocketSession.getId().equals(session.getId())) {
                sessions.remove(webSocketSession);
                break;
            }
        }
    }

    public void sendAlert(final AlertDTO alert) throws ApiException {
        try {
            final TextMessage message = new TextMessage(objectMapper.writeValueAsString(alert));

            for (final WebSocketSession session : sessions) {
                if (session.isOpen()) {
                    session.sendMessage(message);
                }
            }
        } catch (JsonProcessingException e) {
            throw new ApiException("Erreur lors de la sérialisation de l'alerte websocket.", e);
        } catch (IOException ioException) {
            throw new ApiException("Erreur lors de l'envoi de l'alerte websocket.", ioException);
        }
    }

    @Scheduled(fixedRate = 50, timeUnit = TimeUnit.SECONDS)
    public void sendPing() throws ApiException {
        final TextMessage ping = new TextMessage("ping");

        for (final WebSocketSession session : sessions) {
            if (session.isOpen()) {
                try {
                    session.sendMessage(ping);
                } catch (IOException e) {
                    throw new ApiException("Erreur lors de l'envoi du ping websocket.", e);
                }
            }
        }
    }
}
