package hallapinyoMarket.hallapinyoMarketspring.service;

import hallapinyoMarket.hallapinyoMarketspring.domain.Post;
import hallapinyoMarket.hallapinyoMarketspring.repository.PostRepository;
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
    public long save(Post post) {
        postRepository.save(post);
        return post.getId();
    }

    public Post findOne(long postId) {
        return postRepository.findOne(postId);
    }

    public List<Post> findAll(int offset, int limit) {
        return postRepository.findAllPost(offset, limit);
    }
}
