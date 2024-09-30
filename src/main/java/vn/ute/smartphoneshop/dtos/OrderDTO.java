package vn.ute.smartphoneshop.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {

    @NotNull(message = "Order date can't blank")
    private Date orderDate;
    @JsonProperty("userId")
    private int user_id;
    @JsonProperty("voucherId")
    private int voucher_id;
    @NotNull(message = "Order status cannot blank")
    private String orderStatus;
}
