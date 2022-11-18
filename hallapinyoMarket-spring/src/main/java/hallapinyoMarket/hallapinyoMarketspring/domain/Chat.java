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
    private Long id;

    private String contents;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name= "chattingRoom_id")
    private ChattingRoom chattingRoom;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private Member sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private Member receiver;
}
