package vn.ute.smartphoneshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ute.smartphoneshop.entity.ShippingEntity;

@Repository
public interface ShippingRepository extends JpaRepository<ShippingEntity,Integer> {
}
