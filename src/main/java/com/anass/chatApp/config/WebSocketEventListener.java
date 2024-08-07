package com.anass.chatApp.config;


import com.anass.chatApp.chat.ChatMessage;
import com.anass.chatApp.chat.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {

    private final SimpMessageSendingOperations messageSendingOperations;

    //This method handles the event when a WebSocket session is disconnected.
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event){
        // configuring a message to send when a user is diconnected
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        if(username != null){
            log.info("User {} has diconnected", username);
            ChatMessage message = ChatMessage.builder()
                    .type(MessageType.DISCONNECT)
                    .sender(username)
                    .build();
            messageSendingOperations.convertAndSend("/topic/public", message);
        }
    }
}
