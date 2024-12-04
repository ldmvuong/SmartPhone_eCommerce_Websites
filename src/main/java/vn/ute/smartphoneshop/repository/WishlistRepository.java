package vn.ute.smartphoneshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ute.smartphoneshop.entity.WishlistEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishlistRepository extends JpaRepository<WishlistEntity, Integer> {
    List<WishlistEntity> findByUser_UserId(int userId);
    Optional<WishlistEntity> findByProduct_ProductIdAndAndUser_UserId(int productId, int userId);
}
