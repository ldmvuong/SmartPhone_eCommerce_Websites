package vn.ute.smartphoneshop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "payment")
public class PaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int paymentId;

    @ManyToOne
    @JoinColumn(name = "orderId", referencedColumnName = "orderId")
    private OrderEntity order;

    @Column(name = "paymentMethod", nullable = false)
    private String paymentMethod;

    @Column(name = "paymentDate", nullable = false)
    private String paymentDate;

    @Column(name = "amountPaid", nullable = false)
    private double amountPaid;
}
