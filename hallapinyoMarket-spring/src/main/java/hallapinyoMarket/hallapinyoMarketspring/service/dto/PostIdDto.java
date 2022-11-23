package hallapinyoMarket.hallapinyoMarketspring.service.dto;

import hallapinyoMarket.hallapinyoMarketspring.domain.Post;
import lombok.Data;

@Data
public class PostIdDto {
    Long id;

    public PostIdDto(Long id) {
        this.id = id;
    }
}
