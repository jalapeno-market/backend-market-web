package hallapinyoMarket.hallapinyoMarketspring.service.dto;

import hallapinyoMarket.hallapinyoMarketspring.domain.Post;
import hallapinyoMarket.hallapinyoMarketspring.domain.PostStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostManyDto {
    private Long id;
    private String title;
    private String img1;
    private LocalDateTime createdAt;
    private String price;
    private PostStatus status;

    private PostManyDto(Long id, String title, String img1, LocalDateTime createdAt, String price,
                        PostStatus status) {
        this.id = id;
        this.title = title;
        this.img1 = img1;
        this.createdAt = createdAt;
        this.price = price;
        this.status = status;
    }

    public static PostManyDto from(Post post) {
        return new PostManyDto(post.getId(), post.getTitle(), post.getImage().getImg1(), post.getCreatedAt(),
                post.getPrice(), post.getStatus());
    }
}
