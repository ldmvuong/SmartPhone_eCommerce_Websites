package vn.ute.smartphoneshop.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {
    @JsonProperty("paymentId")
    private int id;
    private int orderId;
    @NotNull(message = "Payment method can't blank")
    private String paymentMethod;
    @NotNull(message = "Payment date is required")
    private LocalDateTime paymentDate;
    @NotNull(message = "The amount paid is required")
    private Data amountPaid;
}
