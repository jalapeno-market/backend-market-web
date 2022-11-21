package hallapinyoMarket.hallapinyoMarketspring.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Bad Rest Parameter : don't attempt null referring")
public class RestParameterNullException extends RuntimeException {
}