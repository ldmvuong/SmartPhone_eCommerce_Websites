package vn.ute.smartphoneshop.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.ute.smartphoneshop.entity.PaymentEntity;
import vn.ute.smartphoneshop.entity.VoucherEntity;
import vn.ute.smartphoneshop.enums.OrderStatus;
import vn.ute.smartphoneshop.model.dto.MyOrderDTO;
import vn.ute.smartphoneshop.entity.OrderEntity;
import vn.ute.smartphoneshop.model.request.CartDetailRequest;
import vn.ute.smartphoneshop.model.response.OrderDetaiRespone;
import vn.ute.smartphoneshop.model.response.OrderRespone;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface IOrderService {
    OrderEntity createOrder(int id, BigDecimal totalPrice, VoucherEntity voucher,
                            PaymentEntity payment, Integer cartId,
                            List<CartDetailRequest> cartDetailList);
    List<MyOrderDTO> getOrderHistory(int userId);
    Page<OrderRespone> getAllOrders(Pageable pageable);
    Page<OrderRespone> getOrdersByStatus(OrderStatus status, Pageable pageable);
    Optional<OrderDetaiRespone> getOrderDetailById(int orderID);
}
