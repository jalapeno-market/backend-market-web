package hallapinyoMarket.hallapinyoMarketspring.repository;

import hallapinyoMarket.hallapinyoMarketspring.domain.Chat;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class ChatRepository {
    @PersistenceContext
    private EntityManager em;

    public Long save(Chat chat) {
        em.persist(chat);
        return chat.getId();
    }

    public Chat find(Long id) {
        return em.find(Chat.class, id);
    }

    public List<Chat> findAll() {
        return em.createQuery("select m from Chat m", Chat.class)
                .getResultList();
    }

    /* jpql 배우고 사용해보자. 이건 Member의 예시이다.
    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name",
                        Member.class)
                .setParameter("name", name)
                .getResultList();
    }
    */
}