package hallapinyoMarket.hallapinyoMarketspring.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import hallapinyoMarket.hallapinyoMarketspring.controller.login.SessionConst;
import hallapinyoMarket.hallapinyoMarketspring.domain.Chat;
import hallapinyoMarket.hallapinyoMarketspring.domain.ChattingRoom;
import hallapinyoMarket.hallapinyoMarketspring.domain.Member;
import hallapinyoMarket.hallapinyoMarketspring.exception.RestParameterSelfException;
import hallapinyoMarket.hallapinyoMarketspring.exception.WebSocketExceptionText;
import hallapinyoMarket.hallapinyoMarketspring.exception.WebSocketJsonException;
import hallapinyoMarket.hallapinyoMarketspring.exception.exhandler.ErrorResult;
import hallapinyoMarket.hallapinyoMarketspring.repository.ChatRepository;
import hallapinyoMarket.hallapinyoMarketspring.repository.ChattingRoomRepository;
import hallapinyoMarket.hallapinyoMarketspring.repository.MemberRepository;
import hallapinyoMarket.hallapinyoMarketspring.service.WebSocketService;
import hallapinyoMarket.hallapinyoMarketspring.web.ChatWebSocketForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static hallapinyoMarket.hallapinyoMarketspring.controller.login.SessionConst.LOGIN_MEMBER;

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
    private final WebSocketService webSocketService;
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(IllegalAccessException.class)
    public ErrorResult illegalAccessHandle(IllegalAccessException e) {
        log.error("[exceptionHandle] ex", e);
        return new ErrorResult("UNAUTHORIZED", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult illegalExHandle(IllegalArgumentException e) {
        log.error("[exceptionHandle] ex", e);
        return new ErrorResult("BAD", e.getMessage());
    }
    // message
    @Override
    @Transactional
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Member m = (Member) session.getAttributes().get(LOGIN_MEMBER);
        String payload = message.getPayload();

        ChatWebSocketForm chatWebSocketForm = objectMapper.readValue(payload, ChatWebSocketForm.class);
        webSocketService.errorCheck1(m, session);
        Long myRoomId = chatWebSocketForm.getRoomId();
        Long mySenderId = m.getId();
        Long myReceiverId = chattingRoomRepository.findReceiverIdByRoomIdAndSenderId(myRoomId, mySenderId);
        webSocketService.errorCheck2(m,mySenderId,myReceiverId,chatWebSocketForm,session);

        if(chatWebSocketForm.getType().equals("ENTER")) {
            for(WebSocketSession key : sessionRoomMap.keySet()) {
                if(key == session) {
                    session.sendMessage(WebSocketExceptionText.returnErrorMessage4());
                    throw new WebSocketJsonException();
                }
            }
            sessionRoomMap.put(session, chatWebSocketForm.getRoomId());
        }
        else if(chatWebSocketForm.getType().equals("TALK")) {
            webSocketService.createChat(chatWebSocketForm, mySenderId, myReceiverId);
            for(WebSocketSession key : sessionRoomMap.keySet()) { // 실시간 채팅 코드
                if(sessionRoomMap.get(key).equals(chatWebSocketForm.getRoomId())) {
                    key.sendMessage(message);
                }
            }
        }
        else { // Type 값을 이상하게 주었을때
            session.sendMessage(WebSocketExceptionText.returnErrorMessage5());
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