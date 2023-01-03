package hallapinyoMarket.hallapinyoMarketspring.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import hallapinyoMarket.hallapinyoMarketspring.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class MemberTest {

    @Autowired
    MemberService memberService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mvc;

    @Test
    public void 회원가입_입력값이_잘못된_경우 () throws Exception {
        //given
        Post post = new Post();

        //when
        String body = objectMapper.writeValueAsString(post);

        //then
        mvc.perform(MockMvcRequestBuilders.post("/members/join")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void 회원가입_아이디가_중복되는_경우 () throws Exception {
        //given
        Member member = new Member();
        member.setUserId("test");
        member.setNickname("test");
        member.setPassword("test");
        memberService.join(member);

        //when
        String body = objectMapper.writeValueAsString(member);

        //then
        mvc.perform(MockMvcRequestBuilders.post("/members/join")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void 회원가입_아이디가_빈칸인경우 () throws Exception {
        //given
        Member member = new Member();
        member.setUserId("");
        member.setNickname("test");
        member.setPassword("test");

        //when
        String body = objectMapper.writeValueAsString(member);

        //then
        mvc.perform(MockMvcRequestBuilders.post("/members/join")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void 회원가입_비밀번호가_빈칸인경우 () throws Exception {
        //given
        Member member = new Member();
        member.setUserId("test");
        member.setNickname("test");
        member.setPassword("");

        //when
        String body = objectMapper.writeValueAsString(member);

        //then
        mvc.perform(MockMvcRequestBuilders.post("/members/join")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void 회원가입_정상 () throws Exception {
        //given
        Member member = new Member();
        member.setUserId("newMember");
        member.setNickname("newMember");
        member.setPassword("newMember");

        //when
        String body = objectMapper.writeValueAsString(member);

        //then
        mvc.perform(MockMvcRequestBuilders.post("/members/join")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
