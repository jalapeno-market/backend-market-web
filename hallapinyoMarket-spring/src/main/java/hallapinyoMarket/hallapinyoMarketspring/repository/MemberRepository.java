package hallapinyoMarket.hallapinyoMarketspring.repository;

import hallapinyoMarket.hallapinyoMarketspring.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    public void save(Member member) {em.persist(member);}

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findById(String user_id) {
        return em.createQuery("select m from Member m where m.user_id = :user_id", Member.class)
                .setParameter("user_id", user_id)
                .getResultList();
    }
}
