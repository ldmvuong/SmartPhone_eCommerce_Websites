package vn.ute.smartphoneshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.ute.smartphoneshop.entity.RatingEntity;
import vn.ute.smartphoneshop.model.dto.RatingDTO;
import vn.ute.smartphoneshop.model.response.RatingRespone;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<RatingEntity, Integer> {
    List<RatingEntity> findByProduct_ProductId(int productId);
    @Query("SELECT new vn.ute.smartphoneshop.model.response.RatingRespone(u.lastName || ' ' || u.firstName, p.name, r.content, r.star) " +
            "FROM RatingEntity r " +
            "JOIN r.user u " +
            "JOIN r.product p " +
            "ORDER BY r.id DESC")
    List<RatingRespone> findAllRatings();
}
