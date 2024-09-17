package vn.ute.smartphoneshop.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ute.smartphoneshop.entity.CartDetailEntity;

@Repository
public interface CartDetailRepository extends JpaRepository<CartDetailEntity, Integer> {
    CartDetailEntity findByCartDetailId(int cartDetailId);
}
