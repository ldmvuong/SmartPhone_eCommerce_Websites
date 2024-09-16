package vn.ute.smartphoneshop.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class VoucherDTO {
    @JsonProperty("voucherId")
    private int id;
    @NotNull(message = "Code is required")
    private String code;
    private Double discountPercent;
    private LocalDateTime expirationDate;
}
