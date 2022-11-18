package hallapinyoMarket.hallapinyoMarketspring.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import hallapinyoMarket.hallapinyoMarketspring.controller.login.LoginForm;
import hallapinyoMarket.hallapinyoMarketspring.controller.login.SessionConst;
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
public class Login {

    @Autowired
    MemberService memberService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mvc;

    @BeforeEach
    public void joinMember() {
        Member member = new Member();
        member.setUserId("test");
        member.setNickname("test");
        member.setPassword("test");
        memberService.join(member);
    }

    @Test
    public void 로그인_성공 () throws Exception {
        //given
        LoginForm loginForm = new LoginForm();
        loginForm.setLoginId("test");
        loginForm.setPassword("test");

        //when
        String body = objectMapper.writeValueAsString(loginForm);

        //then
        mvc.perform(MockMvcRequestBuilders.post("/login")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void 로그인_실패 () throws Exception {
        //given
        LoginForm loginForm = new LoginForm();
        loginForm.setLoginId("test");
        loginForm.setPassword("test1");

        //when
        String body = objectMapper.writeValueAsString(loginForm);

        //then
        mvc.perform(MockMvcRequestBuilders.post("/login")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

}
