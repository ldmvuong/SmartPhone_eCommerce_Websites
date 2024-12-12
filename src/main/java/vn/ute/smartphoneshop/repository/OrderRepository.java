package vn.ute.smartphoneshop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.ute.smartphoneshop.entity.OrderEntity;
import vn.ute.smartphoneshop.enums.OrderStatus;
import vn.ute.smartphoneshop.model.response.CustomerSalesDTO;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity,Integer> {
    List<OrderEntity> findByUser_UserIdOrderByOrderDateDesc(int customerId);
    Page<OrderEntity> findByOrderStatusOrderByOrderDateDesc(OrderStatus status, Pageable pageable);
    Page<OrderEntity> findAllByOrderByOrderDateDesc(Pageable pageable);

    @Query("SELECT COUNT(o) FROM OrderEntity o")
    Long countOrders();

    @Query("SELECT COALESCE(SUM(o.totalPrice), 0) FROM OrderEntity o")
    BigDecimal calculateTotalOrderValue();

    @Query("SELECT new vn.ute.smartphoneshop.model.response.CustomerSalesDTO(u.firstName, u.lastName, COUNT(o), SUM(o.totalPrice)) " +
            "FROM OrderEntity o " +
            "JOIN o.user u " +
            "GROUP BY u.userId " +
            "ORDER BY SUM(o.totalPrice) DESC")
    List<CustomerSalesDTO> findTop5Customers();
}
