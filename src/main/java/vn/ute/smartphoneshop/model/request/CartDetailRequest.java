package vn.ute.smartphoneshop.model.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import vn.ute.smartphoneshop.entity.ProductEntity;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CartDetailRequest {
    @Min(value = 1,message = "Cart's ID must be > 0")
    private int cartId;
    @Min(value = 1, message = "Product's ID must be > 0")
    private ProductEntity productId;
    @Min(value = 1,message = "Quantity must be > 0")
    @NotNull(message = "Quantity is required")
    private int quantity;
    @Min(value = 0, message = "Cart's price must be >= 0")
    @NotNull(message = "Cart's price is required")
    private Long cartPrice;
}
