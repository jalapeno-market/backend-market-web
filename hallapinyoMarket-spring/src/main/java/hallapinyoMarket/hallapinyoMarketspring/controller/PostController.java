package hallapinyoMarket.hallapinyoMarketspring.controller;

import hallapinyoMarket.hallapinyoMarketspring.controller.login.SessionConst;
import hallapinyoMarket.hallapinyoMarketspring.domain.Image;
import hallapinyoMarket.hallapinyoMarketspring.domain.Member;
import hallapinyoMarket.hallapinyoMarketspring.domain.Post;
import hallapinyoMarket.hallapinyoMarketspring.exception.exhandler.ErrorResult;
import hallapinyoMarket.hallapinyoMarketspring.service.PostService;
import hallapinyoMarket.hallapinyoMarketspring.service.S3UploaderService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final S3UploaderService s3UploaderService;
    private final PostService postService;

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

    @PostMapping("/post/save")
    public PostIdDto savePost(@ModelAttribute @Valid PostForm postForm, HttpServletRequest request) throws Exception {

        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
            throw new IllegalAccessException("잘못된 접근입니다.");
        }

        List<String> saveImageNames = new ArrayList<>();
        for (MultipartFile image : postForm.getImages()) {
            saveImageNames.add(s3UploaderService.upload(image, "myawsbucketset", "static"));
        }
        if(saveImageNames.size() < 1 || saveImageNames.size() > 3) {
            throw new IllegalArgumentException("잘못된 입력입니다.");
        }

        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        Image image = getImageInstance(saveImageNames);
        Post post = new Post(postForm.title, postForm.contents, image, loginMember, LocalDateTime.now());
        long post_id = postService.save(post);

        return new PostIdDto(post_id);
    }

    private Image getImageInstance(List<String> saveImageNames) { //리팩토링 하기
        if(saveImageNames.size() == 3) {
            return new Image(saveImageNames.get(0), saveImageNames.get(1), saveImageNames.get(2));
        } else if(saveImageNames.size() == 2) {
            return new Image(saveImageNames.get(0), saveImageNames.get(1));
        } else {
            return new Image(saveImageNames.get(0));
        }
    }

    @Data
    static class PostDto {
        private long id;
        private String title;
        private String contents;
        private Image image;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public PostDto(long id, String title, String contents, Image image, LocalDateTime createdAt,
                       LocalDateTime updatedAt) {
            this.id = id;
            this.title = title;
            this.contents = contents;
            this.image = image;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
        }
    }

    @Data
    static class PostForm {
        @NotEmpty
        private String title;
        @NotEmpty
        private String contents;
        private List<MultipartFile> images;
    }

    @Data
    static class PostIdDto {
        private long id;

        public PostIdDto(long id) {
            this.id = id;
        }
    }
}
