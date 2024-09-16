package vn.ute.smartphoneshop.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class ShippingDTO {
    private int shippingId;
    private int orderId;
    @NotNull(message = "Shipping address can't blank")
    private String shippingAddress;
    private LocalDate deliveryDate;
    @NotNull(message = "Shipping type can't blank")
    private String shippingType;
    private String trackingNumber;
}
