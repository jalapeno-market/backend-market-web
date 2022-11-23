package hallapinyoMarket.hallapinyoMarketspring.controller.form;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class PostForm {
    @NotEmpty
    private String title;
    @NotEmpty
    private String contents;
    @NotEmpty
    private List<MultipartFile> images;
    @NotEmpty
    private String price;
}