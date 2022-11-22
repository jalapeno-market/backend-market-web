package hallapinyoMarket.hallapinyoMarketspring.controller;

import hallapinyoMarket.hallapinyoMarketspring.domain.Member;
import hallapinyoMarket.hallapinyoMarketspring.exception.exhandler.ErrorResult;
import hallapinyoMarket.hallapinyoMarketspring.service.MemberService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalStateException.class)
    public ErrorResult illegalExHandle(IllegalStateException e) {
        log.error("[exceptionHandle] ex", e);
        return new ErrorResult("BAD", e.getMessage());
    }

    @PostMapping("/members/join")
    public Result saveMember(@RequestBody @Valid Member member) {
        String user_id = memberService.join(member);
        return new Result(new MemberJoinDto(user_id));
    }

    @GetMapping("/members/{userId}")
    public Result getMemberProfile(@PathVariable("userId") String userId) {
        return new Result(memberService.findMemberProfile(userId));
    }

    @Data
    static class MemberJoinDto {
        private String user_id;

        public MemberJoinDto(String user_id) {
            this.user_id = user_id;
        }
    }
}
