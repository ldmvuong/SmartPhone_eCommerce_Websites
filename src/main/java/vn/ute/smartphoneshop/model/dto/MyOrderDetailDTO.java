package vn.ute.smartphoneshop.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class MyOrderDetailDTO {
    private int product_id;
    private String productName;
    private int quantity;
    private String unitPrice;
}