package hallapinyoMarket.hallapinyoMarketspring.web;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ChatWebSocketForm {
    private String type;
    private Long roomId;
    private String message;
}
