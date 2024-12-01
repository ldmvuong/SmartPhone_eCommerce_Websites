package vn.ute.smartphoneshop.service;

import vn.ute.smartphoneshop.entity.PaymentEntity;
import vn.ute.smartphoneshop.entity.VoucherEntity;
import vn.ute.smartphoneshop.model.dto.MyOrderDTO;
import vn.ute.smartphoneshop.model.dto.OrderDTO;
import vn.ute.smartphoneshop.entity.OrderEntity;
import vn.ute.smartphoneshop.model.dto.UserDTO;
import vn.ute.smartphoneshop.model.request.CartDetailRequest;

import java.math.BigDecimal;
import java.util.List;

public interface IOrderService {
    OrderEntity createOrder(int id, BigDecimal totalPrice, VoucherEntity voucher,
                            PaymentEntity payment, Integer cartId,
                            List<CartDetailRequest> cartDetailList);
    List<MyOrderDTO> getOrderHistory(int userId);
}
