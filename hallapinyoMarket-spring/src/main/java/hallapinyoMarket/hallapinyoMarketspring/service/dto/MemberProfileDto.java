package hallapinyoMarket.hallapinyoMarketspring.service.dto;

import hallapinyoMarket.hallapinyoMarketspring.domain.Member;
import hallapinyoMarket.hallapinyoMarketspring.domain.Post;
import lombok.Data;

@Data
public class MemberProfileDto {
    String userId;
    String nickname;
    int countPost;

    private MemberProfileDto(String userId, String nickname, int countPost) {
        this.userId = userId;
        this.nickname = nickname;
        this.countPost = countPost;
    }

    public static MemberProfileDto from(Member member, int countPost) {
        return new MemberProfileDto(member.getUserId(), member.getNickname(), countPost);
    }
}
