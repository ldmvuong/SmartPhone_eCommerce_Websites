package vn.ute.smartphoneshop.entity;

import jakarta.persistence.*;
import jakarta.persistence.criteria.Order;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "payment")
public class PaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int paymentId;

    @Column(name = "paymentName", nullable = false, length = 255)
    private String name;

    @OneToMany(mappedBy = "payment")
    private List<OrderEntity> orders;

}
