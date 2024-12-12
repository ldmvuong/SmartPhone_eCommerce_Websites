package vn.ute.smartphoneshop.model.response;

import lombok.Getter;
import lombok.Setter;
import vn.ute.smartphoneshop.utils.FormatterUtil;

import java.math.BigDecimal;

@Getter
@Setter
public class CustomerSalesDTO {
    private String firstName;
    private String lastName;
    private Long totalOrders;
    private BigDecimal totalSpent;
    private String priceFormat;

    public CustomerSalesDTO(String firstName, String lastName, Long totalOrders, BigDecimal totalSpent) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.totalOrders = totalOrders;
        this.totalSpent = totalSpent;
        this.priceFormat = FormatterUtil.formatCurrency(totalSpent);
    }
}
