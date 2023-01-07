package hallapinyoMarket.hallapinyoMarketspring.service;

import hallapinyoMarket.hallapinyoMarketspring.controller.login.SessionConst;
import hallapinyoMarket.hallapinyoMarketspring.domain.ChattingRoom;
import hallapinyoMarket.hallapinyoMarketspring.domain.Member;
import hallapinyoMarket.hallapinyoMarketspring.domain.Post;
import hallapinyoMarket.hallapinyoMarketspring.exception.RestParameterNullException;
import hallapinyoMarket.hallapinyoMarketspring.exception.RestParameterOverlapException;
import hallapinyoMarket.hallapinyoMarketspring.exception.RestParameterSelfException;
import hallapinyoMarket.hallapinyoMarketspring.repository.ChattingRoomRepository;
import hallapinyoMarket.hallapinyoMarketspring.repository.MemberRepository;
import hallapinyoMarket.hallapinyoMarketspring.repository.PostRepository;
import hallapinyoMarket.hallapinyoMarketspring.web.PostIdSendForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ChattingRoomService {

    private final ChattingRoomRepository chattingRoomRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    public List<ChattingRoom> findByMemberId(Long memberId) {
        return chattingRoomRepository.findByMemberId(memberId);
    }

    public List<ChattingRoom> findByPostId(Long memberId) {
        return chattingRoomRepository.findByPostId(memberId);
    }

    public PostIdSendForm createChattingRoomByPostId(Long loginMember_id, Long post_id) {

        Member member = memberRepository.find(loginMember_id);
        Post post = postRepository.findOne(post_id);
        List<ChattingRoom> chattingRoomValid = chattingRoomRepository.findByPostIdAndBuyer(post_id, loginMember_id);

        if(member == null || post == null) {
            throw new RestParameterNullException();
        }
        if(!(chattingRoomValid.isEmpty())) {    // 중복 채팅방을 만드려고 할때
            throw new RestParameterOverlapException();
        }
        if(loginMember_id == post.getMember().getId()) {    // 구매자와 판매자가 같은 채팅방을 생성하려 할때
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
    public void deleteChattingRoomById(Long chattingRoom_id) {
        ChattingRoom chattingRoom = chattingRoomRepository.find(chattingRoom_id);
        if(chattingRoom == null) {
            throw new RestParameterNullException();
        }
        chattingRoomRepository.delete(chattingRoom_id);
    }
}
