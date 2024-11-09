package vn.ute.smartphoneshop.model.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    private Integer productId;

    @NotNull(message = "Product's name is required")
    @NotBlank(message = "Product's name cannot be blank")
    private String name;

    @NotNull(message = "Description is required")
    @Size(min = 10, max = 800, message = "Description must be between 10 and 800 characters")
    private String description;

    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price must be greater than or equal to 0")
    private Long price;

    @Min(value = 0, message = "Stock quantity must be greater than or equal to 0")
    private int stockQuantity;

    private Double rating;

    private String status;

    private String imagePath;

    @NotBlank(message = "Battery capacity is required")
    private String batteryCapacity;

    @NotBlank(message = "Processor details are required")
    private String processor;

    private String operatingSystem;

    @NotBlank(message = "SIM details are required")
    private String sim;

    @NotBlank(message = "Connectivity details are required")
    private String connectivity;

    @NotBlank(message = "Camera details are required")
    private String camera;

    @NotBlank(message = "Warranty period is required")
    private String warrantyPeriod;

    @Min(value = 1, message = "Brand ID is required")
    private Long brandId;
}
