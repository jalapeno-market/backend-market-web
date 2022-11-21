package hallapinyoMarket.hallapinyoMarketspring.controller;

import hallapinyoMarket.hallapinyoMarketspring.domain.Member;
import hallapinyoMarket.hallapinyoMarketspring.service.MemberService;
import hallapinyoMarket.hallapinyoMarketspring.service.dto.MemberProfileDto;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/members/join")
    public MemberJoinDto saveMember(@RequestBody @Valid Member member) {
        String user_id = memberService.join(member);
        return new MemberJoinDto(user_id);
    }

    @GetMapping("/members/{userId}")
    public MemberProfileDto getMemberProfile(@PathVariable("userId") String userId) {
        return memberService.findMemberProfile(userId);
    }

    @Data
    static class MemberJoinDto {
        private String user_id;

        public MemberJoinDto(String user_id) {
            this.user_id = user_id;
        }
    }
}
