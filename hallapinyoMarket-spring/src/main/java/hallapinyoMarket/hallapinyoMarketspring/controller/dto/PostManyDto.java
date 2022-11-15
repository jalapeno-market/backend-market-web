package hallapinyoMarket.hallapinyoMarketspring.controller.dto;

import hallapinyoMarket.hallapinyoMarketspring.domain.Post;

import java.time.LocalDateTime;

public class PostManyDto {
    private Long id;
    private String title;
    private String img1;
    LocalDateTime createdAt;
    private String price;

    private PostManyDto(Long id, String title, String img1, LocalDateTime createdAt, String price) {
        this.id = id;
        this.title = title;
        this.img1 = img1;
        this.createdAt = createdAt;
        this.price = price;
    }

    public static PostManyDto from(Post post) {
        return new PostManyDto(post.getId(), post.getTitle(), post.getImage().getImg1(), post.getCreatedAt(),
                post.getPrice());
    }
}
