package hallapinyoMarket.hallapinyoMarketspring.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import hallapinyoMarket.hallapinyoMarketspring.domain.Chat;
import hallapinyoMarket.hallapinyoMarketspring.domain.ChattingRoom;
import hallapinyoMarket.hallapinyoMarketspring.domain.Member;
import hallapinyoMarket.hallapinyoMarketspring.exception.WebSocketJsonException;
import hallapinyoMarket.hallapinyoMarketspring.repository.ChatRepository;
import hallapinyoMarket.hallapinyoMarketspring.repository.ChattingRoomRepository;
import hallapinyoMarket.hallapinyoMarketspring.repository.MemberRepository;
import hallapinyoMarket.hallapinyoMarketspring.web.ChatWebSocketForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
public class ChatHandler extends TextWebSocketHandler {

    private static Map<WebSocketSession, Long> sessionRoomMap = new HashMap<>();
    private final ChatRepository chatRepository;
    private final ObjectMapper objectMapper;
    private final ChattingRoomRepository chattingRoomRepository;
    private final MemberRepository memberRepository;

    // message
    @Override
    @Transactional
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        String payload = message.getPayload();
        log.info("payload : " + payload);

        ChatWebSocketForm chatWebSocketForm = objectMapper.readValue(payload, ChatWebSocketForm.class);

        if(memberRepository.find(chatWebSocketForm.getReceiverId()) == null ||
                memberRepository.find(chatWebSocketForm.getSenderId()) == null ||
                chattingRoomRepository.find(chatWebSocketForm.getRoomId()) == null ||
                chatWebSocketForm.getReceiverId() == chatWebSocketForm.getSenderId()
        ) {
            session.sendMessage(new TextMessage("이상한 ID 값입니다. 웹소켓을 종료합니다."));
            throw new WebSocketJsonException();
        }

        if(chatWebSocketForm.getType().equals(ChatWebSocketForm.MessageType.ENTER)) {
            for(WebSocketSession key : sessionRoomMap.keySet()) {
                if(key == session) {
                    session.sendMessage(new TextMessage("ENTER를 중복하였습니다. 웹소켓을 종료합니다."));
                    throw new WebSocketJsonException();
                }
            }
            sessionRoomMap.put(session, chatWebSocketForm.getRoomId());
        }
        else if(chatWebSocketForm.getType().equals(ChatWebSocketForm.MessageType.TALK)) {
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
                if(sessionRoomMap.get(key).equals(chatWebSocketForm.getRoomId())) {
                    key.sendMessage(message);
                }
            }
        }
        else { // Type 값을 이상하게 주었을때
            new TextMessage("Type 값이 이상합니다. 웹소켓을 종료합니다.");
            throw new WebSocketJsonException();
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
        sessionRoomMap.remove(session);
        log.info(session + " 클라이언트 해제");
    }

}