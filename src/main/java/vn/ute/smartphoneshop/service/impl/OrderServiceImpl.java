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
import vn.ute.smartphoneshop.utils.FormatterUtil;
import vn.ute.smartphoneshop.utils.PriceUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private ICartDetailService cartDetailService;

    @Autowired
    private IPaymentService paymentService;

    @Autowired
    private IUserService userService;

    @Override
    public OrderEntity createOrder(int id, BigDecimal totalPrice, VoucherEntity voucher,
                                   PaymentEntity payment, Integer cartId,
                                   List<CartDetailRequest> cartDetailList) {

        OrderEntity order = new OrderEntity();
        UserEntity user = userService.getUserById(id);
        order.setUser(user);
        order.setTotalPrice(totalPrice);
        order.setAddress(user.getAddress());
        order.setVoucher(voucher);
        order.setPayment(payment);

        orderRepository.save(order);

        for (CartDetailRequest cartDetail : cartDetailList) {
            OrderDetailEntity orderDetail = new OrderDetailEntity();
            orderDetail.setOrder(order);
            orderDetail.setProduct(cartDetail.getProductId());
            orderDetail.setQuantity(cartDetail.getQuantity());
            orderDetail.setUnitPrice(BigDecimal.valueOf(cartDetail.getProductId().getPrice()));

            orderDetailRepository.save(orderDetail);
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

            String formattedDate = FormatterUtil.formatDate(order.getOrderDate());
            orderDTO.setOrderDate(formattedDate);

            orderDTO.setOrderStatus(order.getOrderStatus().toString());
            String formattedTotalPrice = FormatterUtil.formatCurrency(order.getTotalPrice());
            orderDTO.setTotalPrice(formattedTotalPrice);

            List<MyOrderDetailDTO> orderDetails = new ArrayList<>();
            for (OrderDetailEntity detail : order.getOrderDetails()) {
                MyOrderDetailDTO orderDetailDTO = new MyOrderDetailDTO();
                orderDetailDTO.setProductName(detail.getProduct().getName());
                orderDetailDTO.setQuantity(detail.getQuantity());
                String formattedUnitPrice = FormatterUtil.formatCurrency(detail.getUnitPrice());
                orderDetailDTO.setUnitPrice(formattedUnitPrice);
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

    @Override
    public Optional<OrderEntity> getOrderById(int orderId) {
        return orderRepository.findById(orderId);
    }

    @Override
    public void saveOrder(OrderEntity order) {
        orderRepository.save(order);
    }
}
