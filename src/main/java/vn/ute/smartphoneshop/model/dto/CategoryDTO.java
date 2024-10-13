package vn.ute.smartphoneshop.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
    @NotNull(message = "Category's name is required")
    private String categoryName;
    private String description;
}
