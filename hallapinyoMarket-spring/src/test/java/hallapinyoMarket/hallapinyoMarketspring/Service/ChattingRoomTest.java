package hallapinyoMarket.hallapinyoMarketspring.Service;

import hallapinyoMarket.hallapinyoMarketspring.domain.Member;
import hallapinyoMarket.hallapinyoMarketspring.exception.RestParameterNullException;
import hallapinyoMarket.hallapinyoMarketspring.exception.RestParameterOverlapException;
import hallapinyoMarket.hallapinyoMarketspring.exception.RestParameterSelfException;
import hallapinyoMarket.hallapinyoMarketspring.repository.PostRepository;
import hallapinyoMarket.hallapinyoMarketspring.service.ChattingRoomService;
import hallapinyoMarket.hallapinyoMarketspring.service.MemberService;
import hallapinyoMarket.hallapinyoMarketspring.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
public class ChattingRoomTest {

    @Autowired
    MemberService memberService;
    @Autowired
    ChattingRoomService chattingRoomService;
    @Autowired
    PostService postService;

    Long member_id0;
    Long member_id1 = 1L;
    Long post_id0 = 6L;
    Long[] post_id_arr = {1L,2L,3L,4L,5L,6L};
    Long[] member_id_arr = {2L,3L,4L,5L,33L};

    @BeforeEach
    public void joinMember() {
        Member member = new Member();
        member.setUserId("test0");
        member.setNickname("test0");
        member.setPassword("test0");
        memberService.join(member);
        member_id0 = member.getId();
    }

    @Test
    public void 채팅룸_생성 (){
        for(int i = 0 ; i < 6 ; i++) {
            chattingRoomService.createChattingRoomByPostId(member_id0, post_id_arr[i]);
        }
        assertEquals(chattingRoomService.findByMemberId(member_id0).size(),6);
    }
    @Test
    public void 예외_중복_채팅방_생성 (){
        chattingRoomService.createChattingRoomByPostId(member_id0, post_id_arr[0]);
        assertThrows(RestParameterOverlapException.class,
                () -> chattingRoomService.createChattingRoomByPostId(member_id0, post_id_arr[0]));  //예외가 발생해야 한다.
    }
    @Test
    public void 구매자와_판매자가_같은_채팅방을_생성 (){
        chattingRoomService.createChattingRoomByPostId(member_id0, post_id_arr[0]);
        assertThrows(RestParameterSelfException.class,
                () -> chattingRoomService.createChattingRoomByPostId(member_id1, post_id_arr[0]));  //예외가 발생해야 한다.
    }
    @Test
    public void 예외_없는_게시물에서_채팅룸을_생성 (){
        assertThrows(RestParameterNullException.class,
                () -> chattingRoomService.createChattingRoomByPostId(member_id0, 99999999L));       //예외가 발생해야 한다.
    }

    @Test
    public void 해당유저_채팅룸_조회 () {
        for(int i = 0 ; i < 3 ; i++) {
            chattingRoomService.createChattingRoomByPostId(member_id0, post_id_arr[i]);
        }
        assertEquals(chattingRoomService.findByMemberId(member_id0).size(),3);
    }

    @Test
    public void 해당포스트_채팅룸_조회 (){
        for(int i = 0 ; i < 5 ; i++) {
            chattingRoomService.createChattingRoomByPostId(member_id_arr[i], post_id0);
        }
        assertEquals(chattingRoomService.findByPostId(post_id0).size(), 5);
    }

    @Test
    public void 채팅룸_삭제 (){
        for(int i = 0 ; i < 5 ; i++) {
            chattingRoomService.createChattingRoomByPostId(member_id_arr[i], post_id0);
        }
        Long deleteId = chattingRoomService.findByPostId(post_id0).get(0).getId();
        chattingRoomService.deleteChattingRoomById(deleteId);
        assertEquals(chattingRoomService.findByPostId(post_id0).size(), 4);
    }

    @Test
    public void 예외_존재하지_않는_채팅룸_삭제_시도 (){
        for(int i = 0 ; i < 5 ; i++) {
            chattingRoomService.createChattingRoomByPostId(member_id_arr[i], post_id0);
        }
        Long deleteId = 99999999L;
        assertThrows(RestParameterNullException.class,
                () -> chattingRoomService.deleteChattingRoomById(deleteId));
    }
}
