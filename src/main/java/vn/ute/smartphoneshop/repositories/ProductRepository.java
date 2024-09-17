package vn.ute.smartphoneshop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.ute.smartphoneshop.entity.ProductEntity;

public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {
}
