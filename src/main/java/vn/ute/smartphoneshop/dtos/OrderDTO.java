package vn.ute.smartphoneshop.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {

    @NotNull(message = "Order date can't blank")
    private LocalDateTime orderDate;
    private int userId;
    private int voucherId;
    @NotNull(message = "Order status cannot blank")
    private String orderStatus;
}
