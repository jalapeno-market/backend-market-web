package hallapinyoMarket.hallapinyoMarketspring.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Bad Rest Parameter : don't attempt overlapping")
public class RestParameterOverlapException extends RuntimeException {
}