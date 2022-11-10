package hallapinyoMarket.hallapinyoMarketspring.controller;

import hallapinyoMarket.hallapinyoMarketspring.domain.Chat;
import hallapinyoMarket.hallapinyoMarketspring.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
@RequiredArgsConstructor
@Controller
public class ChatController {

    private final ChatRepository chatRepository;
    @GetMapping("/CR/{chattingRoom_id}/chat") // 채팅룸의 챗을 모두 보내준다.
    public List<Chat> returnChatsByChattingRoom(@PathVariable("chattingRoom_id") Long chattingRoom_id) {
        return chatRepository.findByChattingRoomId(chattingRoom_id);
    }


}
