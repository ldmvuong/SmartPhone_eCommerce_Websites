package vn.ute.smartphoneshop.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDTO {
    private int orderId;
    private int productId;
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be > 0")
    private int quantity;
    @NotNull(message = "Unit price can't blank")
    @Min(value = 0, message = "Unit price must be >= 0")
    private Double unitPrice;
}
