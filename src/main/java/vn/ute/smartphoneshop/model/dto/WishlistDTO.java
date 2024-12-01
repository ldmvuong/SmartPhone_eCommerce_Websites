package vn.ute.smartphoneshop.model.dto;

import jakarta.persistence.*;
import lombok.*;
import vn.ute.smartphoneshop.entity.ProductEntity;
import vn.ute.smartphoneshop.entity.UserEntity;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WishlistDTO {
    private int userId;
    private ProductDTO productId;
}
