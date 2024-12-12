package vn.ute.smartphoneshop.vnpay;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.ute.smartphoneshop.entity.CartEntity;
import vn.ute.smartphoneshop.entity.OrderEntity;
import vn.ute.smartphoneshop.entity.PaymentEntity;
import vn.ute.smartphoneshop.entity.VoucherEntity;
import vn.ute.smartphoneshop.model.dto.UserDTO;
import vn.ute.smartphoneshop.model.request.CartDetailRequest;
import vn.ute.smartphoneshop.service.impl.*;
import vn.ute.smartphoneshop.utils.Constant;
import vn.ute.smartphoneshop.utils.SecurityUtil;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping()
public class VNPayController {
    @Autowired
    private VNPayService vnPayService;

    @Autowired
    OrderServiceImpl orderService;

    @Autowired
    UserServiceImpl userService;

    @Autowired
    CartDetailServiceImpl cartDetailService;

    @Autowired
    PaymentServiceImpl paymentService;

    @Autowired
    CartServiceImpl cartService;

    private UserDTO getCurrentUser() {
        String username = SecurityUtil.getCurrentUsername();
        return userService.findByUsername(username);
    }

    // Chuyển hướng người dùng đến cổng thanh toán VNPAY
    @GetMapping({"/user/checkout/vnpay"})
    public String submidOrder(HttpServletRequest request, HttpSession session) {
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        BigDecimal totalPrice = (BigDecimal) session.getAttribute("totalPriceToPayment");
        String vnpayUrl = vnPayService.createOrder(request, totalPrice.intValue(), baseUrl);
        return "redirect:" + vnpayUrl;
    }

    // Sau khi hoàn tất thanh toán, VNPAY sẽ chuyển hướng trình duyệt về URL này
    @GetMapping("/vnpay-payment-return")
    public String paymentCompleted(HttpServletRequest request, Model model, HttpSession session) {
        int paymentStatus =vnPayService.orderReturn(request);

//        String orderInfo = request.getParameter("vnp_OrderInfo");
        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");

        // Định dạng đầu vào
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

        // Định dạng đầu ra
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        // Chuyển chuỗi sang LocalDateTime
        LocalDateTime dateTime = LocalDateTime.parse(paymentTime, inputFormatter);

        // Chuyển LocalDateTime sang chuỗi định dạng mới
        String formattedDate = dateTime.format(outputFormatter);

        String totalPriceVND = totalPrice.substring(0, totalPrice.length() - 2);

        model.addAttribute("orderId", orderInfo);
        model.addAttribute("totalPrice", vn.ute.smartphoneshop.utils.Constant.formatter.format(Long.parseLong(totalPriceVND)));
        model.addAttribute("paymentTime", formattedDate);
        model.addAttribute("transactionId", transactionId);

        if (paymentStatus == 1){
            // Lấy thông tin từ session
            UserDTO user = (UserDTO) getCurrentUser();
            CartEntity cart = (CartEntity) session.getAttribute("cart");
            cart = cartService.findCartByUserId(user.getUserId());
            List<CartDetailRequest> cartDetailToPayment = (List<CartDetailRequest>) session.getAttribute("cartDetailToBuy");
            VoucherEntity voucher = (VoucherEntity) session.getAttribute("voucher");
            PaymentEntity payment = (PaymentEntity) session.getAttribute("payment");
            BigDecimal cartTotalPrice = (BigDecimal) session.getAttribute("totalPriceToPayment"); // Lấy tổng giá đã giảm

            if (user == null || cart == null || cartDetailToPayment == null || cartTotalPrice == null) {
                return "redirect:/user/checkout";
            }

            // Tạo đơn hàng với tổng giá đã giảm
            OrderEntity order = orderService.createOrder(user.getUserId(), cartTotalPrice, voucher, payment, cart.getCartId(), cartDetailToPayment);

            // Xóa các thuộc tính trong session sau khi đã xử lý xong
            session.removeAttribute("cart");
            session.removeAttribute("cartDetails");
            session.removeAttribute("voucher");
            session.removeAttribute("paymentMethod");
            session.removeAttribute("paypalPaymentId");
            session.removeAttribute("cartTotalPrice"); // Xóa tổng giá sau giảm

            return "redirect:/user/my-profile";
        }
        else {
            // Thanh toán không thành công, chuyển hướng quay lại trang checkout
            return "redirect:/user/checkout";
        }
    }

}

