package vn.ute.smartphoneshop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.ute.smartphoneshop.entity.VoucherEntity;

public interface VoucherRepository extends JpaRepository<VoucherEntity, Integer> {
}
