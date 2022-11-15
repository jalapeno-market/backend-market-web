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

    @ManyToOne(fetch = FetchType.LAZY)
    private Member buyer;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member seller;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;
}
