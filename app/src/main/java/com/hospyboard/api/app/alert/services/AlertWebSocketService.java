package com.hospyboard.api.app.alert.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hospyboard.api.app.alert.dto.AlertDTO;
import fr.funixgaming.api.core.exceptions.ApiException;
import jakarta.validation.constraints.NotNull;
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
    private final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").create();

    @Override
    public void afterConnectionEstablished(@NotNull WebSocketSession session) {
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
            final TextMessage message = new TextMessage(gson.toJson(alert));

            for (final WebSocketSession session : sessions) {
                if (session != null && session.isOpen()) {
                    session.sendMessage(message);
                }
            }
        } catch (IOException ioException) {
            throw new ApiException("Erreur lors de l'envoi de l'alerte websocket.", ioException);
        }
    }

    @Scheduled(fixedRate = 50, timeUnit = TimeUnit.SECONDS)
    public void sendPing() throws ApiException {
        final TextMessage ping = new TextMessage("ping");

        for (final WebSocketSession session : sessions) {
            if (session != null && session.isOpen()) {
                try {
                    session.sendMessage(ping);
                } catch (IOException e) {
                    throw new ApiException("Erreur lors de l'envoi du ping websocket.", e);
                }
            }
        }
    }
}
