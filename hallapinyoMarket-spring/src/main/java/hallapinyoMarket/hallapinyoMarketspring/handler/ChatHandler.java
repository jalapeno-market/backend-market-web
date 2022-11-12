package hallapinyoMarket.hallapinyoMarketspring.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import hallapinyoMarket.hallapinyoMarketspring.web.ChatWebSocketForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatHandler extends TextWebSocketHandler {

    private static Map<WebSocketSession, Long> sessionRoomMap = new HashMap<>();
    private final ObjectMapper objectMapper;
    // message
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("payload : " + payload);

        ChatWebSocketForm chatWebSocketForm = objectMapper.readValue(payload, ChatWebSocketForm.class);

        if(chatWebSocketForm.getMessage().equals(ChatWebSocketForm.MessageType.ENTER)) {
            sessionRoomMap.put(session, chatWebSocketForm.getRoomId());
        }
        else if(chatWebSocketForm.getMessage().equals(ChatWebSocketForm.MessageType.TALK)) {
            for(WebSocketSession key : sessionRoomMap.keySet()) {
                if(sessionRoomMap.get(key) == chatWebSocketForm.getRoomId()) { 
                    key.sendMessage(message);
                }
            }
        }
        else {
            sessionRoomMap.remove(session);
        }
    }

    // connection established
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info(session + " 클라이언트 접속");
    }

    // connection closed
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info(session + " 클라이언트 해제");
    }

}