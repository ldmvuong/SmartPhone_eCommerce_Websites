package vn.ute.smartphoneshop.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "`order`")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderId;

    @Column(name = "orderDate", nullable = false)
    private String orderDate;

    @Column(name = "orderStatus", nullable = false)
    private String orderStatus;

    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "voucherId", referencedColumnName = "voucherId")
    private VoucherEntity voucher;
}
