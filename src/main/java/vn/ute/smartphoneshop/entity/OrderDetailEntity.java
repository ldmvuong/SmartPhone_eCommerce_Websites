package vn.ute.smartphoneshop.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@Entity
@Table(name = "orderdetail")
public class OrderDetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderDetailId;

    @ManyToOne
    @JoinColumn(name = "orderId", referencedColumnName = "orderId")
    private OrderEntity order;

    @ManyToOne
    @JoinColumn(name = "productId", referencedColumnName = "productId")
    private ProductEntity product;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "unitPrice", nullable = false)
    private BigDecimal unitPrice;
}
