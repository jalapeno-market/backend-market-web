package hallapinyoMarket.hallapinyoMarketspring.admin;

import hallapinyoMarket.hallapinyoMarketspring.domain.Member;
import hallapinyoMarket.hallapinyoMarketspring.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminService {

    private final MemberRepository memberRepository;

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public Member find(Long id) {
        return memberRepository.find(id);
    }

    @Transactional
    public void updateMember(Long id, String userId, String nickname) {
        Member member = memberRepository.find(id);
        member.change(userId, nickname);
    }

    public boolean checkAdmin(String loginId, String password) {
        return loginId.equals("admin") && password.equals("admin");
    }

    public Member getAdminMember() {
        Member member = new Member();
        member.setUserId("admin");
        member.setPassword("admin");
        return member;
    }
}
