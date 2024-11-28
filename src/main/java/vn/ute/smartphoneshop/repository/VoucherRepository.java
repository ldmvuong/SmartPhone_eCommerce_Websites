package vn.ute.smartphoneshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ute.smartphoneshop.entity.VoucherEntity;

@Repository
public interface VoucherRepository extends JpaRepository<VoucherEntity, Integer> {
    VoucherEntity findByCode(String code);
}
