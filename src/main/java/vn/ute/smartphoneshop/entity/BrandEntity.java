package vn.ute.smartphoneshop.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "brands")
public class BrandEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brandId")
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "brand_img", length = 500)
    private String img;

    @OneToMany(mappedBy = "brand")
    private List<ProductEntity> products;
}
