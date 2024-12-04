package vn.ute.smartphoneshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.ute.smartphoneshop.entity.ProductEntity;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Integer>, JpaSpecificationExecutor<ProductEntity> {
    List<ProductEntity> findByBrandName(String brand);
    @Query("SELECT od.product, SUM(od.quantity) AS totalQuantity " +
            "FROM OrderDetailEntity od " +
            "GROUP BY od.product.productId " +
            "ORDER BY totalQuantity DESC ")
    List<ProductEntity> findByOrderProduct();
}
