package vn.ute.smartphoneshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ute.smartphoneshop.entity.OrderDetailEntity;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetailEntity,Integer> {
    List<OrderDetailEntity> findByProduct_ProductId(int productId);
}
