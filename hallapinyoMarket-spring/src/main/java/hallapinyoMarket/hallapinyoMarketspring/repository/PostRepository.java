package hallapinyoMarket.hallapinyoMarketspring.repository;

import hallapinyoMarket.hallapinyoMarketspring.domain.Post;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class PostRepository {
    @PersistenceContext
    EntityManager em;

    public Long save(Post post) {
        em.persist(post);
        return post.getId();
    }

    public Post find(Long id) {
        return em.find(Post.class, id);
    }

    public List<Post> findAll() {
        return em.createQuery("select m from Post m", Post.class)
                .getResultList();
    }
}
