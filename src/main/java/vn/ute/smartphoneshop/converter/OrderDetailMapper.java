package vn.ute.smartphoneshop.converter;

import vn.ute.smartphoneshop.entity.OrderDetailEntity;
import vn.ute.smartphoneshop.entity.OrderEntity;
import vn.ute.smartphoneshop.model.response.OrderDetaiRespone;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class OrderDetailMapper {

    // Hàm định dạng số tiền
    private static String formatCurrency(BigDecimal amount) {
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###"); // Định dạng số với dấu "," cho phần ngàn
        return decimalFormat.format(amount);
    }

    public static OrderDetaiRespone toOrderDetailRespone(OrderEntity order) {
        OrderDetaiRespone orderDetailDTO = new OrderDetaiRespone();

        orderDetailDTO.setOrderId(order.getOrderId());
        // Lấy tên khách hàng
        String customerName = order.getUser().getFirstName() + " " + order.getUser().getLastName();
        orderDetailDTO.setCustomerName(customerName);

        // Lấy số điện thoại của khách hàng
        String phone = order.getUser().getPhone();
        orderDetailDTO.setPhone(phone);

        // Lấy tổng tiền sau khi giảm (giá đã được giảm)
        BigDecimal totalAfterDiscount = order.getTotalPrice();

        // Voucher giảm giá
        BigDecimal voucherDiscount = order.getVoucher() != null ? BigDecimal.valueOf(order.getVoucher().getDiscountPercent()) : BigDecimal.ZERO;

        // Tính lại tổng tiền trước khi giảm (totalBeforeDiscount)
        BigDecimal totalBeforeDiscount = totalAfterDiscount.divide(BigDecimal.ONE.subtract(voucherDiscount.divide(BigDecimal.valueOf(100))), 2, BigDecimal.ROUND_HALF_UP);

        // Tính số tiền giảm giá
        BigDecimal discountAmount = totalBeforeDiscount.subtract(totalAfterDiscount);

        // Lưu lại các giá trị đã định dạng
        orderDetailDTO.setTotalPrice(formatCurrency(totalBeforeDiscount)); // Tổng tiền trước khi giảm
        orderDetailDTO.setVoucherDiscount(formatCurrency(discountAmount)); // Tiền giảm giá
        orderDetailDTO.setTotalAfterDiscount(formatCurrency(totalAfterDiscount)); // Tổng tiền sau khi giảm

        // Tổng số lượng sản phẩm
        int totalQuantity = order.getOrderDetails().stream()
                .mapToInt(OrderDetailEntity::getQuantity)
                .sum();
        orderDetailDTO.setTotalQuantity(totalQuantity);

        // Phương thức thanh toán
        orderDetailDTO.setPaymentMethod(order.getPayment().getName());

        // Ngày đặt hàng
        orderDetailDTO.setOrderDate(order.getOrderDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));

        // Trạng thái đơn hàng
        orderDetailDTO.setOrderStatus(order.getOrderStatus().toString());

        // Địa chỉ giao hàng
        String address = order.getAddress();
        orderDetailDTO.setAddress(address);

        // Chuyển đổi danh sách chi tiết sản phẩm
        List<OrderDetaiRespone.OrderDetailItemRespone> orderItems = order.getOrderDetails().stream()
                .map(orderDetail -> {
                    OrderDetaiRespone.OrderDetailItemRespone item = new OrderDetaiRespone.OrderDetailItemRespone();
                    item.setProductName(orderDetail.getProduct().getName());
                    item.setProductImage(orderDetail.getProduct().getImagePath());
                    item.setQuantity(orderDetail.getQuantity());
                    item.setUnitPrice(formatCurrency(orderDetail.getUnitPrice())); // Định dạng lại giá sản phẩm
                    return item;
                })
                .collect(Collectors.toList());

        // Gán danh sách sản phẩm cho DTO
        orderDetailDTO.setOrderDetails(orderItems);

        return orderDetailDTO;
    }
}
