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
    private Member member;

    public static Post createPost(String title, String contents, Image image, Member member, LocalDateTime createdAt) {
        Post post = new Post();
        post.setTitle(title);
        post.setContents(contents);
        post.setImage(image);
        post.setMember(member);
        post.setCreatedAt(createdAt);
        return post;
    }
}