package vn.ute.smartphoneshop.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import vn.ute.smartphoneshop.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "`order`")
public class OrderEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderId;

    @Column(name = "orderDate", nullable = false)
    private LocalDateTime orderDate;

    @PrePersist
    public void onPrePersist() {
        this.orderDate = LocalDateTime.now();
        this.orderStatus = OrderStatus.PENDING;
    }
    @Column(name ="address")
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "orderStatus", nullable = false)
    private OrderStatus orderStatus;

    @Column(name = "totalPrice", nullable = false, columnDefinition = "DECIMAL(10, 2)")
    private BigDecimal totalPrice;

    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "voucherId", referencedColumnName = "voucherId")
    private VoucherEntity voucher;

    @ManyToOne
    @JoinColumn(name = "paymentId", referencedColumnName = "paymentId")
    private PaymentEntity payment;

    @OneToMany(mappedBy = "order")
    private List<OrderDetailEntity> orderDetails;

}
