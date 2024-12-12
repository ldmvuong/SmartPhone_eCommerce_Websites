package vn.ute.smartphoneshop.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RatingRespone {
    private String userName;
    private String productName;
    private String content;
    private int star;

    // Constructor
    public RatingRespone(String userName, String productName, String content, int star) {
        this.userName = userName;
        this.productName = productName;
        this.content = content;
        this.star = star;
    }
}
