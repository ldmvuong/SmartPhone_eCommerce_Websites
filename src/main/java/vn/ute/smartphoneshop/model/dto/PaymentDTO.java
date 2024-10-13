package vn.ute.smartphoneshop.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {
    private int orderId;
    @NotNull(message = "Payment method can't blank")
    private String paymentMethod;
    @NotNull(message = "Payment date is required")
    private LocalDateTime paymentDate;
    @NotNull(message = "The amount paid is required")
    private Data amountPaid;
}
