package vn.ute.smartphoneshop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "cart")
@ToString(exclude = "user")
public class CartEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cartId;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private UserEntity user;

    @OneToMany(mappedBy = "cart")
    private List<CartDetailEntity> cartDetailEntityList;

    @Column(name = "total_price",nullable = false, columnDefinition = "DECIMAL(10, 2) DEFAULT 0")
    private Long totalPrice;
}
