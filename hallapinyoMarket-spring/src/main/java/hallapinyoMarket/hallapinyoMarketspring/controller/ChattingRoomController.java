package hallapinyoMarket.hallapinyoMarketspring.controller;

import hallapinyoMarket.hallapinyoMarketspring.domain.ChattingRoom;
import hallapinyoMarket.hallapinyoMarketspring.domain.Member;
import hallapinyoMarket.hallapinyoMarketspring.domain.Post;
import hallapinyoMarket.hallapinyoMarketspring.exception.RestParameterException;
import hallapinyoMarket.hallapinyoMarketspring.repository.ChattingRoomRepository;
import hallapinyoMarket.hallapinyoMarketspring.repository.MemberRepository;
import hallapinyoMarket.hallapinyoMarketspring.repository.PostRepository;
import hallapinyoMarket.hallapinyoMarketspring.web.PostIdSendForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Transactional
@Slf4j
public class ChattingRoomController {

    private final ChattingRoomRepository chattingRoomRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;


    @GetMapping("member/{member_id}/ChattingRoom") // 해당 유저의 채팅룸을 모두 보여준다.
    public List<ChattingRoom> returnChattingRoomsByMember(@PathVariable("member_id") Long member_id) {
        return chattingRoomRepository.findByMemberId(member_id);
    }


    @GetMapping("post/{post_id}/ChattingRoom") // 해당 포스트의 채팅룸을 모두 보여준다.
    public List<ChattingRoom> returnChattingRoomsByPost(@PathVariable("post_id") Long post_id) {
        return chattingRoomRepository.findByPostId(post_id);
    }


    @PostMapping("member/{member_id}/post/{post_id}/ChattingRoom") // 해당 포스트를 이용해 채팅룸을 생성한다.
    public PostIdSendForm createChattingRoomByPost(@PathVariable("post_id") Long post_id, @PathVariable("member_id") Long member_id) {
        Member member = memberRepository.find(member_id);
        Post post = postRepository.find(post_id);
        List<ChattingRoom> chattingRoomValid = chattingRoomRepository.findByPostIdAndBuyer(post_id, member_id);

        if(member == null || post == null) {
            throw new RestParameterException();
        }

        if(!(chattingRoomValid.isEmpty())) {    // 중복 채팅방을 만드려고 할때
            throw new RestParameterException();
        }

        ChattingRoom chattingRoom = new ChattingRoom();
        chattingRoom.setBuyer(member);
        chattingRoom.setSeller(post.getMember());
        chattingRoom.setPost(post);

        PostIdSendForm form = new PostIdSendForm();
        form.setId(chattingRoomRepository.save(chattingRoom));
        return form;
    }

    @DeleteMapping("ChattingRoom/{chattingRoom_id}") // 해당 채팅룸을 삭제한다.
    public void deleteChattingRoom(@PathVariable("chattingRoom_id") Long chattingRoom_id) {
        ChattingRoom chattingRoom = chattingRoomRepository.find(chattingRoom_id);
        if(chattingRoom == null) {
            throw new RestParameterException();
        }
        chattingRoomRepository.delete(chattingRoom_id);
    }
}
