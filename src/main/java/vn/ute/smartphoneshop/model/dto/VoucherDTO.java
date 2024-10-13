package vn.ute.smartphoneshop.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class VoucherDTO {
    @NotNull(message = "Code is required")
    private String code;
    private Double discountPercent;
    private LocalDateTime expirationDate;
}
