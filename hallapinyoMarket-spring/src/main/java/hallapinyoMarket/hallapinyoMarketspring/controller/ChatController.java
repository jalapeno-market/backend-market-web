package hallapinyoMarket.hallapinyoMarketspring.controller;

import hallapinyoMarket.hallapinyoMarketspring.controller.login.SessionConst;
import hallapinyoMarket.hallapinyoMarketspring.domain.Chat;
import hallapinyoMarket.hallapinyoMarketspring.exception.exhandler.ErrorResult;
import hallapinyoMarket.hallapinyoMarketspring.repository.ChatRepository;
import hallapinyoMarket.hallapinyoMarketspring.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
@RequiredArgsConstructor
@Transactional
@RestController
@Slf4j
public class ChatController {

    private final ChatService chatService;
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

    @GetMapping("/chatting-rooms/{chattingRoom_id}/chats") // 채팅룸의 챗을 모두 보내준다.
    public List<Chat> returnChatsByChattingRoom(@PathVariable("chattingRoom_id") Long chattingRoom_id, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession(false);
        validAuthorized(session);

        return chatService.getChatsByChattingRoom(chattingRoom_id);
    }

    private void validAuthorized(HttpSession session) throws IllegalAccessException {
        if(session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
            throw new IllegalAccessException("잘못된 접근입니다.");
        }
    }
}
