package vn.ute.smartphoneshop.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "cartdetail")
public class CartDetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cartDetailId;

    @ManyToOne
    @JoinColumn(name = "cartId", referencedColumnName = "cartId")
    private CartEntity cart;

    @ManyToOne
    @JoinColumn(name = "productId", referencedColumnName = "productId")
    private ProductEntity product;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "cartPrice", nullable = false)
    private double cartPrice;
}
