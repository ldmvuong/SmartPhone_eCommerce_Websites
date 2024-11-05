package vn.ute.smartphoneshop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "product")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "productId")
    private int productId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "stockQuantity")
    private int stockQuantity;

    @Column(name = "rating")
    private float rating;

    @Column(name = "status", nullable = false)
    private String status = "Còn hàng";

    @Column(name = "imagePath")
    private String imagePath;

    @Column(name = "batteryCapacity")
    private String batteryCapacity;

    @Column(name = "processor")
    private String processor;

    @Column(name = "operatingSystem")
    private String operatingSystem;

    @Column(name = "sim")
    private String sim;

    @Column(name = "connectivity")
    private String connectivity;

    @Column(name = "camera")
    private String camera;

    @Column(name = "warrantyPeriod")
    private String warrantyPeriod;

    @ManyToOne
    @JoinColumn(name = "brandId", referencedColumnName = "brandId")
    private BrandEntity brand;
}
