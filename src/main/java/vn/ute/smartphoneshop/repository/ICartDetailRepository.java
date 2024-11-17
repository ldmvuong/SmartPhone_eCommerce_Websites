package vn.ute.smartphoneshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ute.smartphoneshop.entity.CartDetailEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface ICartDetailRepository extends JpaRepository<CartDetailEntity, Integer> {
    List<CartDetailEntity> findByCart_CartId(Integer id);
    Optional<CartDetailEntity> findByCart_CartIdAndAndProduct_ProductId(Integer cart_id, Integer product_id);
}
