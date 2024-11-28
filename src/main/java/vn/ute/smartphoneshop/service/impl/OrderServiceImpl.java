package vn.ute.smartphoneshop.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.ute.smartphoneshop.entity.*;
import vn.ute.smartphoneshop.model.dto.OrderDTO;
import vn.ute.smartphoneshop.exception.DataNotFoundException;
import vn.ute.smartphoneshop.model.dto.UserDTO;
import vn.ute.smartphoneshop.model.request.CartDetailRequest;
import vn.ute.smartphoneshop.repository.OrderDetailRepository;
import vn.ute.smartphoneshop.repository.OrderRepository;
import vn.ute.smartphoneshop.repository.UserRepository;
import vn.ute.smartphoneshop.repository.VoucherRepository;
import vn.ute.smartphoneshop.service.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private OrderRepository orderRepository; // Assuming you have a repository to handle OrderEntity

    @Autowired
    private OrderDetailRepository orderDetailRepository; // Repository for handling OrderDetailEntity

    @Autowired
    private ICartDetailService cartDetailService; // Service to get cart details

    @Autowired
    private IPaymentService paymentService; // Service for payment methods

    @Autowired
    private IUserService userService;

    @Override
    public OrderEntity createOrder(int id, BigDecimal totalPrice, VoucherEntity voucher,
                                   PaymentEntity payment, Integer cartId,
                                   List<CartDetailRequest> cartDetailList) {

        // Create a new OrderEntity and set its basic attributes
        OrderEntity order = new OrderEntity();
        UserEntity user = userService.getUserById(id);
        order.setUser(user);
        order.setTotalPrice(totalPrice);
        order.setAddress(user.getAddress()); // Set shipping address
        order.setVoucher(voucher); // Attach voucher if provided
        order.setPayment(payment); // Attach the selected payment method

        // Save the order to the database
        orderRepository.save(order);

        // Create OrderDetailEntity objects from the cart details
        for (CartDetailRequest cartDetail : cartDetailList) {
            OrderDetailEntity orderDetail = new OrderDetailEntity();
            orderDetail.setOrder(order); // Set the order for this detail
            orderDetail.setProduct(cartDetail.getProductId()); // Set the product
            orderDetail.setQuantity(cartDetail.getQuantity()); // Set quantity
            orderDetail.setUnitPrice(BigDecimal.valueOf(cartDetail.getProductId().getPrice())); // Set unit price of product

            // Save each order detail in the database
            orderDetailRepository.save(orderDetail);
            // After saving, you can update the cart to reflect that it's been ordered (optional)
        }
        cartDetailService.deleteAllByCartId(cartId);
        return order;
    }
}
