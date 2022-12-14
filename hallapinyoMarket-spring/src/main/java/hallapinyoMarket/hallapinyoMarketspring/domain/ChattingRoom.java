package hallapinyoMarket.hallapinyoMarketspring.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class ChattingRoom {

    @Id @GeneratedValue
    @Column(name = "chattingRoom_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private Member buyer;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Member seller;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    /*
        // 양방향 연관 매핑을 하고 싶다면 주석을 푼다 //
        // 주의사항 -> 연관관계 편의 메소드도 작성하자 //
        @OneToMany(mappedBy = "chattingRoom")
        List<Chat> Chats = new ArrayList<Chat>();
    */
}
