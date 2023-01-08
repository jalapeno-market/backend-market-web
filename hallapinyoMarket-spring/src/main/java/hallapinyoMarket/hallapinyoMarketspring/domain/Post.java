package hallapinyoMarket.hallapinyoMarketspring.domain;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

import static javax.persistence.FetchType.*;

@Entity
@Getter @Setter
public class Post {

    @Id
    @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    private String title;

    private String contents;

    @Embedded
    private Image image;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String price;

    @Enumerated(EnumType.STRING)
    private PostStatus status;

    private Boolean is_deleted;

    public static Post of(String title, String contents, Image image, Member member, LocalDateTime createdAt,
                          String price, PostStatus status, Boolean is_deleted) {
        Post post = new Post();
        post.setTitle(title);
        post.setContents(contents);
        post.setImage(image);
        post.setMember(member);
        post.setCreatedAt(createdAt);
        post.setPrice(price);
        post.setStatus(status);
        post.setIs_deleted(is_deleted);
        return post;
    }
}