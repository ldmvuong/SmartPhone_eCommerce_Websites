package vn.ute.smartphoneshop.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRespone {
    private int orderId;
    private String customerName;
    private String totalPrice;
    private int totalQuantity;
    private String paymentMethod;
    private String orderDate;
    private String orderStatus;
    private String productName;
    private String productImage;
}
