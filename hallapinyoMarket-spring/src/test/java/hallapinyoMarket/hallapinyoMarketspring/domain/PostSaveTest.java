package hallapinyoMarket.hallapinyoMarketspring.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import hallapinyoMarket.hallapinyoMarketspring.controller.form.PostForm;
import hallapinyoMarket.hallapinyoMarketspring.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.util.ArrayList;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class PostSaveTest {

    @Autowired
    PostService postService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mvc;

    @Test
    public void 게시물저장_입력값이_잘못된_경우 () throws Exception {
        //given
        Member member = new Member();

        //when
        String body = objectMapper.writeValueAsString(member);

        //then
        mvc.perform(MockMvcRequestBuilders.post("/post")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
