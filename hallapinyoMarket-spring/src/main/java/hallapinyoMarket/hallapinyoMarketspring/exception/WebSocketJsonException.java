package hallapinyoMarket.hallapinyoMarketspring.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Bad Request by WebSocket")
public class WebSocketJsonException extends RuntimeException {
}