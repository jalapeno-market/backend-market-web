package hallapinyoMarket.hallapinyoMarketspring.service;

import hallapinyoMarket.hallapinyoMarketspring.domain.Member;
import hallapinyoMarket.hallapinyoMarketspring.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public String join(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getUser_id();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByUserId(member.getUser_id());
        if(!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    public Member findOne(String user_id) {
        return memberRepository.findByUserId(user_id).get(0);
    }
}
