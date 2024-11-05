package vn.ute.smartphoneshop.model.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BrandDTO {
    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "brand_img", length = 500)
    private String img;
}
