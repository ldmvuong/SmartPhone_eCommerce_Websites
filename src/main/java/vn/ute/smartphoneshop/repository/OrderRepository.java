package vn.ute.smartphoneshop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.ute.smartphoneshop.entity.OrderEntity;
import vn.ute.smartphoneshop.enums.OrderStatus;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity,Integer> {
    List<OrderEntity> findByUser_UserIdOrderByOrderDateDesc(int customerId);
    Page<OrderEntity> findByOrderStatus(OrderStatus status, Pageable pageable);

    @Query("SELECT COUNT(o) FROM OrderEntity o")
    Long countOrders();

    @Query("SELECT COALESCE(SUM(o.totalPrice), 0) FROM OrderEntity o")
    BigDecimal calculateTotalOrderValue();
}
