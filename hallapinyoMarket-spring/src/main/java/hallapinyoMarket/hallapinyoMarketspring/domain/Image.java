package hallapinyoMarket.hallapinyoMarketspring.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Image {

    private String img1;

    private String img2;

    private String img3;

    protected Image() {
    }

    public Image(String img1, String img2, String img3) {
        this.img1 = img1;
        this.img2 = img2;
        this.img3 = img3;
    }
}
