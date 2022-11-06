package hallapinyoMarket.hallapinyoMarketspring.controller;

import hallapinyoMarket.hallapinyoMarketspring.domain.Member;
import hallapinyoMarket.hallapinyoMarketspring.service.MemberService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/members/join")
    public MemberJoinDto saveMember(@RequestBody @Valid Member member) {
        Long id = memberService.join(member);
        return new MemberJoinDto(id);
    }

    @Data
    static class MemberJoinDto {
        private Long id;

        public MemberJoinDto(Long id) {
            this.id = id;
        }
    }
}
