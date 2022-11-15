package hallapinyoMarket.hallapinyoMarketspring.repository;

import hallapinyoMarket.hallapinyoMarketspring.domain.Post;
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

    public List<Post> findAllPost(int offset, int limit) {
        return em.createQuery("select p from Post p" +
                        " order by p.createdAt desc")
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }
}
