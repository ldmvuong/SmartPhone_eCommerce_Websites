package vn.ute.smartphoneshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ute.smartphoneshop.entity.CartDetailEntity;

import java.util.List;

@Repository
public interface ICartDetailRepository extends JpaRepository<CartDetailEntity, Integer> {
    List<CartDetailEntity> findByCart_CartId(Integer id);
}
