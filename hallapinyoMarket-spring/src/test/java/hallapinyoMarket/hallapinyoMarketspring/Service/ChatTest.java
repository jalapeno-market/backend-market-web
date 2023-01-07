package hallapinyoMarket.hallapinyoMarketspring.Service;

import hallapinyoMarket.hallapinyoMarketspring.repository.ChatRepository;
import hallapinyoMarket.hallapinyoMarketspring.repository.MemberRepository;
import hallapinyoMarket.hallapinyoMarketspring.service.ChatService;
import hallapinyoMarket.hallapinyoMarketspring.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
public class ChatTest {
    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ChatService chatService;

    @Autowired
    ChatRepository chatRepository;

    @BeforeEach
    public void 전제사항() {
    }

    @Test
    public void 앤_되잖아 (){
        memberRepository.find(1L);
    }

    @Test
    public void 채팅_조회 (){
        chatRepository.find(1L);
    }
}
