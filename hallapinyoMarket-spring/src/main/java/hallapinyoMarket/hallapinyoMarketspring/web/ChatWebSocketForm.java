package hallapinyoMarket.hallapinyoMarketspring.web;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ChatWebSocketForm {

    public enum MessageType {
        ENTER, TALK, QUIT
    }

    private MessageType type;
    private Long roomId;
    private Long senderId;
    private Long receiverId;
    private String message;
}
