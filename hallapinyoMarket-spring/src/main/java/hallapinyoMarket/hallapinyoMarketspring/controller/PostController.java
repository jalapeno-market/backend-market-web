package hallapinyoMarket.hallapinyoMarketspring.controller;

import hallapinyoMarket.hallapinyoMarketspring.controller.form.PostForm;
import hallapinyoMarket.hallapinyoMarketspring.service.dto.PostIdDto;
import hallapinyoMarket.hallapinyoMarketspring.service.dto.PostManyDto;
import hallapinyoMarket.hallapinyoMarketspring.controller.login.SessionConst;
import hallapinyoMarket.hallapinyoMarketspring.domain.Image;
import hallapinyoMarket.hallapinyoMarketspring.domain.Member;
import hallapinyoMarket.hallapinyoMarketspring.domain.Post;
import hallapinyoMarket.hallapinyoMarketspring.domain.PostStatus;
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
import java.util.stream.Collectors;

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

    @PostMapping("/post")
    public Result savePost(@ModelAttribute @Valid PostForm postForm, HttpServletRequest request) throws Exception {

        HttpSession session = request.getSession(false);
        validAuthorized(session);
        validateImagesSize(postForm.getImages());
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

        Long post_id = postService.save(postForm, loginMember);

        return new Result(new PostIdDto(post_id));
    }

    private static void validateImagesSize(List<MultipartFile> images) {
        if(images.size() < 1 || images.size() > 3) {
            throw new IllegalArgumentException("잘못된 입력입니다.");
        }
    }

    @GetMapping("/post/{postId}")
    public Result getPost(@PathVariable("postId") Long postId, HttpServletRequest request) throws Exception{

        validAuthorized(request.getSession(false));
        return new Result(postService.findOne(postId));
    }

    @GetMapping("/posts")
    public Result getPosts(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "10") int limit,
            HttpServletRequest request) throws Exception
    {
        validAuthorized(request.getSession(false));
        List<Post> posts = postService.findAll(offset, limit);
        List<PostManyDto> collect = posts.stream()
                .map(p -> PostManyDto.from(p))
                .collect(Collectors.toList());

        return new Result(collect);
    }

    @GetMapping("/posts/{userId}")
    public Result getPostsOfUserId(
            @PathVariable("userId") String userId,
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "10") int limit,
            HttpServletRequest request) throws Exception
    {
        validAuthorized(request.getSession(false));
        List<Post> posts = postService.findAllByUserId(userId, offset, limit);
        List<PostManyDto> collect = posts.stream()
                .map(p -> PostManyDto.from(p))
                .collect(Collectors.toList());

        return new Result(collect);
    }

    @PostMapping("/post/status/{postId}")
    public Result soldStatusPost(@PathVariable("postId") Long postId, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession(false);
        validPostHost(postService.findOne(postId).getUserId(), session);

        return new Result(postService.changeStatus(postId));
    }

    @DeleteMapping("/post/{postId}")
    public Result deletePost(@PathVariable("postId") Long postId, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession(false);
        validPostHost(postService.findOne(postId).getUserId(), session);

        return new Result(postService.deleteOne(postId));
    }

/*    @PatchMapping("/post/{postId}")
    public Result updatePost(@ModelAttribute @Valid PostForm postForm,
                             @PathVariable("postId") Long postId,
                             HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession(false);
        validPostHost(postService.findOne(postId).getUserId(), session);
        validateImagesSize(postForm.getImages());

        return new Result(postService.updatePost(postForm, ))
    }*/

    private void validAuthorized(HttpSession session) throws IllegalAccessException {
        if(session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
            throw new IllegalAccessException("잘못된 접근입니다.");
        }
    }

    private void validPostHost(String userId, HttpSession session) throws IllegalAccessException {
        validAuthorized(session);
        Member member = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if(!member.getUserId().equals(userId)) {
            throw new IllegalAccessException("잘못된 접근입니다.");
        }
    }
}
