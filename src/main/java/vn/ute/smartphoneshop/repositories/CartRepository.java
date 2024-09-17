package vn.ute.smartphoneshop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ute.smartphoneshop.entity.CartEntity;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, Integer> {
}
