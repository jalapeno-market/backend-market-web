package hallapinyoMarket.hallapinyoMarketspring.service;

import hallapinyoMarket.hallapinyoMarketspring.domain.Post;
import hallapinyoMarket.hallapinyoMarketspring.domain.PostStatus;
import hallapinyoMarket.hallapinyoMarketspring.repository.PostRepository;
import hallapinyoMarket.hallapinyoMarketspring.repository.dto.PostIdDto;
import hallapinyoMarket.hallapinyoMarketspring.repository.dto.PostOneDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public Long save(Post post) {
        postRepository.save(post);
        return post.getId();
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
}
