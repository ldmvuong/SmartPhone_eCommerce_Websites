package vn.ute.smartphoneshop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ute.smartphoneshop.entity.VoucherEntity;

@Repository
public interface VoucherRepository extends JpaRepository<VoucherEntity, Integer> {
}
