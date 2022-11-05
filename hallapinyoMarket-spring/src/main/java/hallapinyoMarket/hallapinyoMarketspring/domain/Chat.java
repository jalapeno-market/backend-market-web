package hallapinyoMarket.hallapinyoMarketspring.domain;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
public class Chat {

    @Id @GeneratedValue
    @Column(name = "chat_id")
    private long id;

    private String contents;

    @ManyToOne(fetch = LAZY)
    private ChattingRoom chattingRoom;

    @ManyToOne(fetch = LAZY)
    private Member sender;

    @ManyToOne(fetch = LAZY)
    private Member receiver;
}
