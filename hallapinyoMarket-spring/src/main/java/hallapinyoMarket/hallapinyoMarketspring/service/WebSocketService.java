package hallapinyoMarket.hallapinyoMarketspring.service;

import hallapinyoMarket.hallapinyoMarketspring.domain.Chat;
import hallapinyoMarket.hallapinyoMarketspring.domain.ChattingRoom;
import hallapinyoMarket.hallapinyoMarketspring.domain.Member;
import hallapinyoMarket.hallapinyoMarketspring.exception.WebSocketExceptionText;
import hallapinyoMarket.hallapinyoMarketspring.exception.WebSocketJsonException;
import hallapinyoMarket.hallapinyoMarketspring.repository.ChatRepository;
import hallapinyoMarket.hallapinyoMarketspring.repository.ChattingRoomRepository;
import hallapinyoMarket.hallapinyoMarketspring.repository.MemberRepository;
import hallapinyoMarket.hallapinyoMarketspring.repository.PostRepository;
import hallapinyoMarket.hallapinyoMarketspring.web.ChatWebSocketForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
@Slf4j
public class WebSocketService {
    private final ChattingRoomRepository chattingRoomRepository;
    private final MemberRepository memberRepository;
    private final ChatRepository chatRepository;

    public void createChat(ChatWebSocketForm chatWebSocketForm, Long sender_id, Long receiver_id) {
        ChattingRoom chattingRoom = chattingRoomRepository.find(chatWebSocketForm.getRoomId());
        Member sender = memberRepository.find(sender_id);
        Member receiver = memberRepository.find(receiver_id);


        Chat chat = new Chat();
        chat.setChattingRoom(chattingRoom);
        chat.setContents(chatWebSocketForm.getMessage());
        chat.setSender(sender);
        chat.setReceiver(receiver);
        chat.setCreatedAt(LocalDateTime.now());
        chat.setUpdatedAt(LocalDateTime.now());

        chatRepository.save(chat);
    }

    public void errorCheck1(Member m, WebSocketSession session) throws Exception{
        if(m == null) {
            session.sendMessage(WebSocketExceptionText.returnErrorMessage1());
            throw new WebSocketJsonException();
        }
    }

    public void errorCheck2(Member m, Long mySenderId, Long myReceiverId, ChatWebSocketForm chatWebSocketForm, WebSocketSession session) throws Exception {
        if(memberRepository.find(myReceiverId) == null ||
                memberRepository.find(mySenderId) == null ||
                chattingRoomRepository.find(chatWebSocketForm.getRoomId()) == null ||
                myReceiverId == mySenderId
        ) {
            session.sendMessage(WebSocketExceptionText.returnErrorMessage2());
            throw new WebSocketJsonException();
        }

        if(!(m.getUserId().equals(chatWebSocketForm.getSenderUserId()))) {
            session.sendMessage(WebSocketExceptionText.returnErrorMessage3());
            throw new WebSocketJsonException();
        }
    }
}
