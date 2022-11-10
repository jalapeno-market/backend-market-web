package hallapinyoMarket.hallapinyoMarketspring.repository;

import hallapinyoMarket.hallapinyoMarketspring.domain.Chat;
import hallapinyoMarket.hallapinyoMarketspring.domain.ChattingRoom;
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
        return em.createQuery("select c from Chat c", Chat.class)
                .getResultList();
    }

    public List<Chat> findByChattingRoomId(Long chattingRoom_id) {
        return em.createQuery(
                        "Select c from Chat c " +
                                "where c.chattingRoom.id = :chattingRoom_id", Chat.class)
                .setParameter("chattingRoom_id", chattingRoom_id)
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