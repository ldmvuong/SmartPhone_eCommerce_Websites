package vn.ute.smartphoneshop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.ute.smartphoneshop.converter.OrderDetailMapper;
import vn.ute.smartphoneshop.converter.OrderMapper;
import vn.ute.smartphoneshop.entity.*;
import vn.ute.smartphoneshop.enums.OrderStatus;
import vn.ute.smartphoneshop.model.dto.MyOrderDTO;
import vn.ute.smartphoneshop.model.dto.MyOrderDetailDTO;
import vn.ute.smartphoneshop.model.request.CartDetailRequest;
import vn.ute.smartphoneshop.model.response.OrderDetaiRespone;
import vn.ute.smartphoneshop.model.response.OrderRespone;
import vn.ute.smartphoneshop.repository.OrderDetailRepository;
import vn.ute.smartphoneshop.repository.OrderRepository;
import vn.ute.smartphoneshop.service.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @Override
    public List<MyOrderDTO> getOrderHistory(int userId) {
        List<OrderEntity> orders = orderRepository.findByUser_UserIdOrderByOrderDateDesc(userId);
        List<MyOrderDTO> orderDTOs = new ArrayList<>();

        for (OrderEntity order : orders) {
            MyOrderDTO orderDTO = new MyOrderDTO();
            orderDTO.setOrderId(order.getOrderId());
            orderDTO.setOrderDate(order.getOrderDate());
            orderDTO.setOrderStatus(order.getOrderStatus().toString());
            orderDTO.setTotalPrice(order.getTotalPrice());

            List<MyOrderDetailDTO> orderDetails = new ArrayList<>();
            for (OrderDetailEntity detail : order.getOrderDetails()) {
                MyOrderDetailDTO orderDetailDTO = new MyOrderDetailDTO();
                orderDetailDTO.setProductName(detail.getProduct().getName());
                orderDetailDTO.setQuantity(detail.getQuantity());
                orderDetailDTO.setUnitPrice(detail.getUnitPrice());
                orderDetails.add(orderDetailDTO);
            }

            orderDTO.setOrderDetails(orderDetails);
            orderDTOs.add(orderDTO);
        }
        return orderDTOs;
    }

    @Override
    public Page<OrderRespone> getAllOrders(Pageable pageable) {
        Page<OrderEntity> orderEntities = orderRepository.findAll(pageable);
        return orderEntities.map(OrderMapper::toOrderRespone);
    }

    @Override
    public Page<OrderRespone> getOrdersByStatus(OrderStatus status, Pageable pageable) {
        Page<OrderEntity> orderEntities = orderRepository.findByOrderStatus(status , pageable);
        return orderEntities.map(OrderMapper::toOrderRespone);
    }

    @Override
    public Optional<OrderDetaiRespone> getOrderDetailById(int orderID) {
        Optional<OrderEntity> order = orderRepository.findById(orderID);

        return order.map(OrderDetailMapper::toOrderDetailRespone);

    }

}
