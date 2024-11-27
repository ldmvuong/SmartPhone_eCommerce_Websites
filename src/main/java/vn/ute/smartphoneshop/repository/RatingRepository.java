package vn.ute.smartphoneshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ute.smartphoneshop.entity.RatingEntity;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<RatingEntity, Integer> {
    List<RatingEntity> findByProductId(Long productId);

}
