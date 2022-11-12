package hallapinyoMarket.hallapinyoMarketspring.controller;

import hallapinyoMarket.hallapinyoMarketspring.domain.ChattingRoom;
import hallapinyoMarket.hallapinyoMarketspring.domain.Member;
import hallapinyoMarket.hallapinyoMarketspring.domain.Post;
import hallapinyoMarket.hallapinyoMarketspring.repository.ChattingRoomRepository;
import hallapinyoMarket.hallapinyoMarketspring.repository.MemberRepository;
import hallapinyoMarket.hallapinyoMarketspring.repository.PostRepository;
import hallapinyoMarket.hallapinyoMarketspring.web.PostIdSendForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class ChattingRoomController {

    private final ChattingRoomRepository chattingRoomRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    @ResponseBody
    @GetMapping("member/{member_id}/ChattingRoom") // 해당 유저의 채팅룸을 모두 보여준다.
    public List<ChattingRoom> returnChattingRoomsByMember(@PathVariable("member_id") Long member_id) {
        return chattingRoomRepository.findByMemberId(member_id);
    }

    @ResponseBody
    @GetMapping("post/{post_id}/ChattingRoom") // 해당 포스트의 채팅룸을 모두 보여준다.
    public List<ChattingRoom> returnChattingRoomsByPost(@PathVariable("post_id") Long post_id) {
        return chattingRoomRepository.findByPostId(post_id);
    }

    @ResponseBody
    @PostMapping("member/{member_id}/post/{post_id}/ChattingRoom") // 해당 포스트를 이용해 채팅룸을 생성한다.
    public PostIdSendForm createChattingRoomByPost(@PathVariable("post_id") Long post_id, @PathVariable("member_id") Long member_id) {
        Member member = memberRepository.find(member_id);
        Post post = postRepository.find(post_id);

        ChattingRoom chattingRoom = new ChattingRoom();
        chattingRoom.setBuyer(member);
        chattingRoom.setSeller(post.getMember());
        chattingRoom.setPost(post);

        PostIdSendForm form = new PostIdSendForm();
        form.setId(chattingRoomRepository.save(chattingRoom));
        return form;
    }
}
