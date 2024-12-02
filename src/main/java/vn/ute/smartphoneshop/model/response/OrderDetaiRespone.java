package vn.ute.smartphoneshop.model.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class OrderDetaiRespone {

    private int orderId;               // Mã đơn hàng
    private String customerName;       // Tên khách hàng
    private String totalPrice;         // Tổng tiền trước khi áp dụng voucher
    private String totalAfterDiscount; // Tổng tiền sau khi áp dụng voucher
    private String voucherDiscount;    // Voucher giảm giá
    private int totalQuantity;         // Tổng số lượng sản phẩm
    private String paymentMethod;      // Phương thức thanh toán
    private String orderDate;          // Ngày đặt hàng
    private String orderStatus;        // Trạng thái đơn hàng
    private String address;            // Địa chỉ giao hàng
    private String phone; // Add phone field
    private List<OrderDetailItemRespone> orderDetails; // Danh sách sản phẩm trong đơn hàng

    // DTO cho chi tiết sản phẩm
    @Getter
    @Setter
    public static class OrderDetailItemRespone {
        private String productName;      // Tên sản phẩm
        private String productImage;     // Hình ảnh sản phẩm
        private int quantity;            // Số lượng sản phẩm
        private String unitPrice;    // Giá đơn vị của sản phẩm
    }
}