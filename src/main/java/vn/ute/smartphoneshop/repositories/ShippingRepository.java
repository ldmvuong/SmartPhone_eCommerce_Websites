package vn.ute.smartphoneshop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.ute.smartphoneshop.entity.ShippingEntity;

public interface ShippingRepository extends JpaRepository<ShippingEntity,Integer> {
}
