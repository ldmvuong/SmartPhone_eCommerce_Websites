package vn.ute.smartphoneshop.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductSalesDTO {
    private String productName;
    private String imagePath;
    private Long totalQuantitySold;

    public ProductSalesDTO(String productName, String imagePath, Long totalQuantitySold) {
        this.productName = productName;
        this.imagePath = imagePath;
        this.totalQuantitySold = totalQuantitySold;
    }
}
