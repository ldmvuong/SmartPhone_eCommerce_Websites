package vn.ute.smartphoneshop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.ute.smartphoneshop.entity.PaymentEntity;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Integer> {
}
