package hallapinyoMarket.hallapinyoMarketspring.repository;

import hallapinyoMarket.hallapinyoMarketspring.domain.ChattingRoom;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
        return em.createQuery("select m from ChattingRoom m", ChattingRoom.class)
                .getResultList();
    }
}
