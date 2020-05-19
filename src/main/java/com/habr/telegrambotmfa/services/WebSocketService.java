package com.habr.telegrambotmfa.services;

import com.habr.telegrambotmfa.login.AuthenticationInfo;
import com.habr.telegrambotmfa.repositories.WebSocketSessionStorage;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketService {
    private SimpMessagingTemplate simpMessagingTemplate;
    private WebSocketSessionStorage sessionStorage;

    public WebSocketService(SimpMessagingTemplate simpMessagingTemplate, WebSocketSessionStorage sessionStorage) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.sessionStorage = sessionStorage;
    }

    public void sendLoginStatus(AuthenticationInfo info, String csrf) {
        String sessionId = sessionStorage.getSessionId(csrf);
        if (info.isSuccess()) {
            sessionStorage.removeSession(csrf);
        }

        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);

        simpMessagingTemplate.convertAndSendToUser(sessionId, "/queue/login", info, headerAccessor.getMessageHeaders());
    }
}
