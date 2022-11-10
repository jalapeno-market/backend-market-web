package hallapinyoMarket.hallapinyoMarketspring.repository;

import hallapinyoMarket.hallapinyoMarketspring.domain.ChattingRoom;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class ChattingRoomRepository {
    @PersistenceContext
    private EntityManager em;

    public Long save(ChattingRoom chattingRoom) {
        em.persist(chattingRoom);
        return chattingRoom.getId();
    }

    public ChattingRoom find(Long id) {
        return em.find(ChattingRoom.class, id);
    }

    public List<ChattingRoom> findAll() {
        return em.createQuery("select c from ChattingRoom c", ChattingRoom.class)
                .getResultList();
    }

    public List<ChattingRoom> findByMemberId(Long member_id) {
        return em.createQuery(
                    "Select c from ChattingRoom c " +
                            "where c.buyer.id = :member_id " +
                            "or c.seller.id = :member_id", ChattingRoom.class
                )
                .setParameter("member_id", member_id)
                .getResultList();
    }

    public List<ChattingRoom> findByPostId(Long post_id) {
        return em.createQuery(
                        "Select c from ChattingRoom c " +
                                "where c.post.id = :post_id ", ChattingRoom.class
                )
                .setParameter("post_id", post_id)
                .getResultList();
    }
}
