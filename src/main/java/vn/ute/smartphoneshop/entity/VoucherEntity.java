package vn.ute.smartphoneshop.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "voucher")
public class VoucherEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int voucherId;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "discountPercent")
    private float discountPercent;

    @Column(name = "expiryDate")
    private String expiryDate;
}
