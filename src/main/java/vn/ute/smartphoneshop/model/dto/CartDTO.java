package vn.ute.smartphoneshop.model.dto;

import jakarta.validation.constraints.Min;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class CartDTO {
    @Min(value = 1, message = "User's ID must be > 0")
    private int userId;
}
