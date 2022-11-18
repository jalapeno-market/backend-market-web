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
}
