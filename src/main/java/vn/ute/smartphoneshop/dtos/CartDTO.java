package vn.ute.smartphoneshop.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class CartDTO {
    @JsonProperty("cartId")
    private int id;
    @Min(value = 1, message = "User's ID must be > 0")
    private int userId;
}
