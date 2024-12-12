package vn.ute.smartphoneshop.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Setter
@Getter
@Table(name = "ratings")
public class RatingEntity {
    @Id
    @Column(name = "ratingId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 65535)
    private String content;

    @Column(nullable = false)
    private int star;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "productId", nullable = false)
    private ProductEntity product;

    @ManyToOne
    @JoinColumn(name = "orderId", nullable = false)
    private OrderEntity order;

    public RatingEntity(String content, int star, UserEntity user, ProductEntity product, OrderEntity order) {
        this.content = content;
        this.star = star;
        this.user = user;
        this.product = product;
        this.order = order;
    }
}