package vn.ute.smartphoneshop.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class MyOrderDTO {
    private int orderId;
    private String orderDate;
    private String orderStatus;
    private String totalPrice;
    private List<MyOrderDetailDTO> orderDetails;
}
