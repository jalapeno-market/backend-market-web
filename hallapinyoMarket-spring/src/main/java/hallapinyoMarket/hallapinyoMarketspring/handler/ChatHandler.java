package hallapinyoMarket.hallapinyoMarketspring.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import hallapinyoMarket.hallapinyoMarketspring.domain.Chat;
import hallapinyoMarket.hallapinyoMarketspring.domain.ChattingRoom;
import hallapinyoMarket.hallapinyoMarketspring.domain.Member;
import hallapinyoMarket.hallapinyoMarketspring.repository.ChatRepository;
import hallapinyoMarket.hallapinyoMarketspring.repository.ChattingRoomRepository;
import hallapinyoMarket.hallapinyoMarketspring.repository.MemberRepository;
import hallapinyoMarket.hallapinyoMarketspring.web.ChatWebSocketForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatHandler extends TextWebSocketHandler {

    private static Map<WebSocketSession, Long> sessionRoomMap = new HashMap<>();
    private final ChatRepository chatRepository;
    private final ObjectMapper objectMapper;
    private final ChattingRoomRepository chattingRoomRepository;
    private final MemberRepository memberRepository;

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

            ChattingRoom chattingRoom = chattingRoomRepository.find(chatWebSocketForm.getRoomId());
            Member sender = memberRepository.find(chatWebSocketForm.getSenderId());
            Member receiver = memberRepository.find(chatWebSocketForm.getReceiverId());


            Chat chat = new Chat();
            chat.setChattingRoom(chattingRoom);
            chat.setContents(chatWebSocketForm.getMessage());
            chat.setSender(sender);
            chat.setReceiver(receiver);
            chat.setCreatedAt(LocalDateTime.now());
            chat.setUpdatedAt(LocalDateTime.now());

            chatRepository.save(chat);
            
            for(WebSocketSession key : sessionRoomMap.keySet()) { // 실시간 채팅 코드
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