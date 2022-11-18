package hallapinyoMarket.hallapinyoMarketspring.repository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hallapinyoMarket.hallapinyoMarketspring.domain.Post;
import hallapinyoMarket.hallapinyoMarketspring.domain.PostStatus;
import hallapinyoMarket.hallapinyoMarketspring.domain.QMember;
import hallapinyoMarket.hallapinyoMarketspring.domain.QPost;
import hallapinyoMarket.hallapinyoMarketspring.repository.dto.PostIdDto;
import hallapinyoMarket.hallapinyoMarketspring.repository.dto.PostManyDto;
import hallapinyoMarket.hallapinyoMarketspring.repository.dto.PostOneDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostRepository {

    private final EntityManager em;

    public void save(Post post) {
        em.persist(post);
    }

    public Post findOne(Long id) {
        return em.find(Post.class, id);
    }

    public List<Post> findAllPostByUserId(String userId, int offset, int limit) {
        QPost post = QPost.post;
        JPAQueryFactory query = new JPAQueryFactory(em);

        return query
                .select(post)
                .from(post)
                .where(post.member.userId.eq(userId))
                .orderBy(post.createdAt.desc())
                .offset(offset)
                .limit(limit)
                .fetch();
    }

    public List<Post> findAllPost(int offset, int limit) {
        QPost post = QPost.post;

        JPAQueryFactory query = new JPAQueryFactory(em);

        return query
                .select(post)
                .from(post)
                .where(post.status.eq(PostStatus.SALE))
                .orderBy(post.createdAt.desc())
                .offset(offset)
                .limit(limit)
                .fetch();
    }
}
