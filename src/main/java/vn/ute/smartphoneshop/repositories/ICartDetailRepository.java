package vn.ute.smartphoneshop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ute.smartphoneshop.entity.CartDetailEntity;

@Repository
public interface ICartDetailRepository extends JpaRepository<CartDetailEntity, Integer> {
//    CartDetailEntity findByCartDetailId(int cartDetailId);
//    void insertCartDetail(CartDetailEntity cartDetailEntity);
//    void deleteCartDetail(CartDetailEntity cartDetailEntity);
//    void updateCartDetail(CartDetailEntity cartDetailEntity);
}
