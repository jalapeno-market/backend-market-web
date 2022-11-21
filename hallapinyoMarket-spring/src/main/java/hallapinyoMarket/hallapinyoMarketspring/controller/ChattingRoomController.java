package hallapinyoMarket.hallapinyoMarketspring.controller;

import hallapinyoMarket.hallapinyoMarketspring.controller.login.SessionConst;
import hallapinyoMarket.hallapinyoMarketspring.domain.ChattingRoom;
import hallapinyoMarket.hallapinyoMarketspring.domain.Member;
import hallapinyoMarket.hallapinyoMarketspring.domain.Post;
import hallapinyoMarket.hallapinyoMarketspring.exception.RestParameterNullException;
import hallapinyoMarket.hallapinyoMarketspring.exception.RestParameterOverlapException;
import hallapinyoMarket.hallapinyoMarketspring.exception.RestParameterSelfException;
import hallapinyoMarket.hallapinyoMarketspring.exception.exhandler.ErrorResult;
import hallapinyoMarket.hallapinyoMarketspring.repository.ChattingRoomRepository;
import hallapinyoMarket.hallapinyoMarketspring.repository.MemberRepository;
import hallapinyoMarket.hallapinyoMarketspring.repository.PostRepository;
import hallapinyoMarket.hallapinyoMarketspring.web.PostIdSendForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Transactional
@Slf4j
public class ChattingRoomController {

    private final ChattingRoomRepository chattingRoomRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

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
    @GetMapping("/chatting_rooms") // 해당 유저의 채팅룸을 모두 보여준다.
    public List<ChattingRoom> returnChattingRoomsByMember(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession(false);
        validAuthorized(session);

        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

        return chattingRoomRepository.findByMemberId(loginMember.getId());
    }


    @GetMapping("posts/{post_id}/chatting_rooms") // 해당 포스트의 채팅룸을 모두 보여준다.
    public List<ChattingRoom> returnChattingRoomsByPost(@PathVariable("post_id") Long post_id, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession(false);
        validAuthorized(session);

        return chattingRoomRepository.findByPostId(post_id);
    }


    @PostMapping("/posts/{post_id}/chatting_rooms") // 해당 포스트를 이용해 채팅룸을 생성한다.
    public PostIdSendForm createChattingRoomByPost(@PathVariable("post_id") Long post_id, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession(false);
        validAuthorized(session);
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

        Member member = memberRepository.find(loginMember.getId());
        Post post = postRepository.findOne(post_id);
        List<ChattingRoom> chattingRoomValid = chattingRoomRepository.findByPostIdAndBuyer(post_id, loginMember.getId());

        if(member == null || post == null) {
            throw new RestParameterNullException();
        }

        if(!(chattingRoomValid.isEmpty())) {    // 중복 채팅방을 만드려고 할때
            throw new RestParameterOverlapException();
        }

        if(loginMember.getId() == post.getMember().getId()) {    // 구매자와 판매자가 같은 채팅방을 생성하려 할때
            throw new RestParameterSelfException();
        }

        ChattingRoom chattingRoom = new ChattingRoom();
        chattingRoom.setBuyer(member);
        chattingRoom.setSeller(post.getMember());
        chattingRoom.setPost(post);

        PostIdSendForm form = new PostIdSendForm();
        form.setId(chattingRoomRepository.save(chattingRoom));
        return form;
    }

    @DeleteMapping("chatting_rooms/{chattingRoom_id}") // 해당 채팅룸을 삭제한다.
    public void deleteChattingRoom(@PathVariable("chattingRoom_id") Long chattingRoom_id, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession(false);
        validAuthorized(session);

        ChattingRoom chattingRoom = chattingRoomRepository.find(chattingRoom_id);
        if(chattingRoom == null) {
            throw new RestParameterNullException();
        }
        chattingRoomRepository.delete(chattingRoom_id);
    }

    private void validAuthorized(HttpSession session) throws IllegalAccessException {
        if(session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
            throw new IllegalAccessException("잘못된 접근입니다.");
        }
    }
}
