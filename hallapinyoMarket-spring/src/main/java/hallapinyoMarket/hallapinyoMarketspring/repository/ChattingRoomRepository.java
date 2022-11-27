package hallapinyoMarket.hallapinyoMarketspring.repository;

import hallapinyoMarket.hallapinyoMarketspring.domain.Chat;
import hallapinyoMarket.hallapinyoMarketspring.domain.ChattingRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChattingRoomRepository {
    @PersistenceContext
    private EntityManager em;

    private final ChatRepository chatRepository;

    public Long save(ChattingRoom chattingRoom) {
        em.persist(chattingRoom);
        return chattingRoom.getId();
    }

    public ChattingRoom find(Long id) {
        return em.find(ChattingRoom.class, id);
    }

    public void delete(Long id) {
        ChattingRoom chattingRoom = em.find(ChattingRoom.class, id);
        List<Chat> chatList = chatRepository.findAll();

        for(Chat c : chatList) {
            if(c.getChattingRoom().getId() == id) {
                em.remove(c);
            }
        }
        em.remove(chattingRoom);
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

    public List<ChattingRoom> findByPostIdAndBuyer(Long post_id, Long buyer_id) {
        return em.createQuery(
                        "Select c from ChattingRoom c " +
                                "where c.post.id = :post_id and c.buyer.id = :buyer_id ", ChattingRoom.class
                )
                .setParameter("post_id", post_id)
                .setParameter("buyer_id", buyer_id)
                .getResultList();
    }

    public Long findReceiverIdByRoomIdAndSenderId(Long room_id, Long sender_id) {
        ChattingRoom chattingRoom = em.createQuery(
                "Select c from ChattingRoom c where c.id = :room_id", ChattingRoom.class)
                .setParameter("room_id", room_id)
                .getResultList()
                .get(0);
        if(chattingRoom.getBuyer().getId() == sender_id) {
            return chattingRoom.getSeller().getId();
        }
        else {
            return chattingRoom.getBuyer().getId();
        }
    }
}
