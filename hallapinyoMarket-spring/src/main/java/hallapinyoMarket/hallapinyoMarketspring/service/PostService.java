package hallapinyoMarket.hallapinyoMarketspring.service;

import hallapinyoMarket.hallapinyoMarketspring.controller.form.PostForm;
import hallapinyoMarket.hallapinyoMarketspring.domain.Image;
import hallapinyoMarket.hallapinyoMarketspring.domain.Member;
import hallapinyoMarket.hallapinyoMarketspring.domain.Post;
import hallapinyoMarket.hallapinyoMarketspring.domain.PostStatus;
import hallapinyoMarket.hallapinyoMarketspring.repository.PostRepository;
import hallapinyoMarket.hallapinyoMarketspring.service.dto.PostIdDto;
import hallapinyoMarket.hallapinyoMarketspring.service.dto.PostOneDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final S3UploaderService s3UploaderService;

    @Transactional
    public Long save(PostForm postForm, Member member) throws IOException{

        List<String> imageNames = getImageNames(postForm);
        Image image = getImageInstance(imageNames);
        Post post = Post.of(postForm.getTitle(), postForm.getContents(), image, member, LocalDateTime.now(),
                postForm.getPrice(), PostStatus.SALE);

        postRepository.save(post);
        return post.getId();
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

    private List<String> getImageNames(PostForm postform) throws IOException {
        List<String> saveImageNames = new ArrayList<>();
        for (MultipartFile image : postform.getImages()) {
            saveImageNames.add(s3UploaderService.upload(image, "myawsbucketset", "static"));
        }
        return saveImageNames;
    }

    @Transactional
    public List<Post> findAllByUserId(String userId, int offset, int limit) {
        return postRepository.findAllPostByUserId(userId, offset, limit);
    }

    @Transactional
    public PostIdDto changeStatus(Long id) {
        Post post = postRepository.findOne(id);
        if(post.getStatus().equals(PostStatus.SALE)) {
            post.setStatus(PostStatus.SOLD);
        } else {
            post.setStatus(PostStatus.SALE);
        }

        return new PostIdDto(post.getId());
    }

    public PostOneDto findOne(Long postId) {
        return PostOneDto.from(postRepository.findOne(postId));
    }

    public List<Post> findAll(int offset, int limit) {
        return postRepository.findAllPost(offset, limit);
    }

    @Transactional
    public PostIdDto deleteOne(Long postId) {
        Post findPost = postRepository.findOne(postId);
        postRepository.deletePost(findPost);
        return new PostIdDto(findPost.getId());
    }

/*    @Transactional
    public PostIdDto updatePost(PostForm post, Long postId) {
        Post findPost = postRepository.findOne(postId);
        findPost.setTitle(post.getTitle());
        findPost.setContents(post.getContents());
        findPost.setPrice(post.getPrice());
        return new PostIdDto(postId);
    }*/
}
