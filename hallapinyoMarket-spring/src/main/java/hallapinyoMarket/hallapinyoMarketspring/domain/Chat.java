package hallapinyoMarket.hallapinyoMarketspring.domain;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
public class Chat {

    @Id @GeneratedValue
    @Column(name = "chat_id")
    private long id;

    private String contents;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name= "chattingRoom_id")
    private ChattingRoom chattingRoom;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "sender_id")
    private Member sender;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "receiver_id")
    private Member receiver;
}
