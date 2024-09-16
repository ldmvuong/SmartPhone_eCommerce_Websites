package vn.ute.smartphoneshop.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "shipping")
public class ShippingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int shippingId;

    @ManyToOne
    @JoinColumn(name = "orderId", referencedColumnName = "orderId")
    private OrderEntity order;

    @Column(name = "shippingAddress", nullable = false)
    private String shippingAddress;

    @Column(name = "deliveryDate")
    private String deliveryDate;

    @Column(name = "shippingType", nullable = false)
    private String shippingType;

    @Column(name = "trackingNumber")
    private String trackingNumber;
}
