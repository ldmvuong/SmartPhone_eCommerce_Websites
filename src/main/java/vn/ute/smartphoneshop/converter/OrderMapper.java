package vn.ute.smartphoneshop.converter;

import vn.ute.smartphoneshop.entity.OrderDetailEntity;
import vn.ute.smartphoneshop.entity.OrderEntity;
import vn.ute.smartphoneshop.model.response.OrderRespone;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

public class OrderMapper {
    public static OrderRespone toOrderRespone(OrderEntity order) {
        OrderRespone orderRespone = new OrderRespone();
        String customerName = order.getUser().getFirstName() + " " + order.getUser().getLastName();
        orderRespone.setCustomerName(customerName);
        orderRespone.setOrderId(order.getOrderId());

        BigDecimal totalPrice = order.getTotalPrice();
        DecimalFormat df = new DecimalFormat("###,###,###");
        String formattedPrice = df.format(totalPrice);
        orderRespone.setTotalPrice(formattedPrice);

        // Total Quantity: Sum of quantities in OrderDetails
        int totalQuantity = order.getOrderDetails().stream()
                .mapToInt(OrderDetailEntity::getQuantity)
                .sum();
        orderRespone.setTotalQuantity(totalQuantity);

        // Payment Method
        orderRespone.setPaymentMethod(order.getPayment().getName());

        // Order Date: Chuyển đổi LocalDateTime thành String theo định dạng
        orderRespone.setOrderDate(order.getOrderDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));

        // Order Status
        orderRespone.setOrderStatus(order.getOrderStatus().toString());

        // Get the first product from the order details
        if (!order.getOrderDetails().isEmpty()) {
            OrderDetailEntity firstOrderDetail = order.getOrderDetails().get(0);
            orderRespone.setProductName(firstOrderDetail.getProduct().getName());
            orderRespone.setProductImage(firstOrderDetail.getProduct().getImagePath());
        }
        return orderRespone;
    }
}
