package vn.ute.smartphoneshop.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    @JsonProperty("productId")
    private int id;
    @NotNull(message = "Product's name is required")
    private String name;
    private String brand;
    private String description;
    @NotNull(message = "Price of product is required")
    @Min(value = 0, message = "Price of product must be >= 0")
    private Double price;
    @Min(value = 0,message = "Stock of quantity must be greater than or equal 0")
    private int stockQuantity;
    @Min(value = 0, message = "Rating must be >=0")
    @Max(value = 5, message = "Rating must be <=5")
    private Double rating;
    private String status;
    private String imagePath;
    private String batteryCapacity;
    private String processor;
    private String operatingSystem;
    private String sim;
    private String connectivity;
    private String camera;
    private String warrantyPeriod;
    private int categoryId;
}
