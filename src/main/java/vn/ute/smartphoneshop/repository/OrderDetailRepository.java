package vn.ute.smartphoneshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.ute.smartphoneshop.entity.OrderDetailEntity;
import vn.ute.smartphoneshop.model.response.ProductSalesDTO;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetailEntity,Integer> {
    List<OrderDetailEntity> findByProduct_ProductId(int productId);
    @Query("SELECT new vn.ute.smartphoneshop.model.response.ProductSalesDTO(p.name, p.imagePath, SUM(od.quantity)) " +
            "FROM OrderDetailEntity od " +
            "JOIN od.product p " +
            "GROUP BY p.productId " +
            "ORDER BY SUM(od.quantity) DESC")
    List<ProductSalesDTO> findTop5BestSellingProducts();
}
