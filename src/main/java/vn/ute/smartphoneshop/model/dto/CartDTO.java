package vn.ute.smartphoneshop.model.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import vn.ute.smartphoneshop.entity.CartDetailEntity;
import vn.ute.smartphoneshop.entity.UserEntity;

import java.math.BigDecimal;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class CartDTO {
    @Min(value = 1, message = "User's ID must be > 0")
    private int userId;

    @Min(value = 0, message = "Stock quantity must be greater than or equal to 0")
    private Long totalPrice;
}
