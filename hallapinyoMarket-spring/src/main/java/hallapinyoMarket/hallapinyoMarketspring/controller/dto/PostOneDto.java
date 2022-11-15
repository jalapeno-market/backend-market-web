package hallapinyoMarket.hallapinyoMarketspring.controller.dto;

import hallapinyoMarket.hallapinyoMarketspring.domain.Image;
import hallapinyoMarket.hallapinyoMarketspring.domain.Post;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostOneDto {
    private Long id;
    private String title;
    private String contents;
    private Image image;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String userId;
    private String nickname;

    private PostOneDto(Long id, String title, String contents, Image image,
                      LocalDateTime createdAt, LocalDateTime updatedAt, String userId, String nickname) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.image = image;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.userId = userId;
        this.nickname = nickname;
    }

    public static PostOneDto from(Post post) {
        return new PostOneDto(post.getId(), post.getTitle(), post.getContents(), post.getImage(),
                post.getCreatedAt(), post.getUpdatedAt(), post.getMember().getUserId(), post.getMember().getNickname());
    }
}
