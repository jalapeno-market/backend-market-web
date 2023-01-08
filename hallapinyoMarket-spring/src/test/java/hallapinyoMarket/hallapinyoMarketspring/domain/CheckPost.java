package hallapinyoMarket.hallapinyoMarketspring.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import hallapinyoMarket.hallapinyoMarketspring.controller.login.SessionConst;
import hallapinyoMarket.hallapinyoMarketspring.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class CheckPost {

    @Autowired
    PostService postService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mvc;

    MockHttpSession mockHttpSession;
    
    @BeforeEach
    public void setSession() {
        Member member = new Member();
        member.setUserId("test");
        member.setPassword("test");
        member.setNickname("test");
        mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute(SessionConst.LOGIN_MEMBER, member);
    }

    @Test
    public void 게시물조회_전체_비로그인유저인_경우 () throws Exception {
        //then
        mvc.perform(MockMvcRequestBuilders.get("/posts"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void 게시물조회_전체_성공() throws Exception {
        //then
        mvc.perform(MockMvcRequestBuilders.get("/posts")
                .session(mockHttpSession))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void 게시물조회_하나_비로그인유저인_경우 () throws Exception {
        //then
        mvc.perform(MockMvcRequestBuilders.get("/post/2"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void 게시물조회_존재하지않거나_삭제된_게시물인_경우 () throws  Exception {
        //given
        mvc.perform(MockMvcRequestBuilders.get("/post/1000")
                        .session(mockHttpSession))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
