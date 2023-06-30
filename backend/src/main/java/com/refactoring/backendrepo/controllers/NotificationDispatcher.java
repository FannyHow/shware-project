package com.netcracker.ncfallprojectrepo.controllers;

import com.netcracker.ncfallprojectrepo.exceptions.ResourceNotFoundException;
import com.netcracker.ncfallprojectrepo.module.Message;
import com.netcracker.ncfallprojectrepo.services.UserService;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.HashMap;

@Component
@EnableScheduling
@Log4j2
public class NotificationDispatcher {

    @Autowired
    UserService userService;

    @Getter
    @Setter
    private volatile static HashMap<String, String> principalNameToSockSessionMap = new HashMap<>();

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    public void sendToUser(String principalName,String destination,Notification notification) throws NotificationException {

        if(!principalNameToSockSessionMap.containsKey(principalName)){
            throw new NotificationException(String.format("Can not get session for principal name `%s` as there is no session in RAM map.",principalName));
        }

        String sessionId = principalNameToSockSessionMap.get(principalName);

        log.info("Sending targeted notification to {} - {}", principalName, sessionId);
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);
        simpMessagingTemplate.convertAndSendToUser(
                sessionId,
                destination!=null?destination:"/notification/item",
                notification,
                headerAccessor.getMessageHeaders());
    }


    @EventListener
    public void sessionDisconnectHandler(SessionDisconnectEvent sessionDisconnectEvent) {
        String sessionId = sessionDisconnectEvent.getSessionId();
        log.info("Disconnecting : {}", sessionId);
        principalNameToSockSessionMap.remove(sessionId);
        log.info("Current Sessions Count : {}", principalNameToSockSessionMap.size());
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        logger.info("Received a new web socket connection");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String username = (String) headerAccessor.getSessionAttributes().get("username");
        if(username != null) {
            logger.info("User Disconnected : " + username);

            Message message = new Message();
            message.setUser(userService.findUserByUsername(username)
                    .orElseThrow(() -> new ResourceNotFoundException("Error: User Not Found")));

            messagingTemplate.convertAndSend("/topic/public", message);
        }
    }

    @Data
    public static class Notification {

        private final String value;

        public Notification(String s) {
            this.value = s;
        }
    }

    public static class NotificationException extends Exception{

        public NotificationException(String s) {
            super(s);
        }
    }
}
