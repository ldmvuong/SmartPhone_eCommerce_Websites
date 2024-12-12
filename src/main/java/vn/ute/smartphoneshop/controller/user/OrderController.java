package vn.ute.smartphoneshop.controller.user;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.ute.smartphoneshop.entity.*;
import vn.ute.smartphoneshop.model.dto.UserDTO;
import vn.ute.smartphoneshop.model.request.CartDetailRequest;
import vn.ute.smartphoneshop.service.*;
import vn.ute.smartphoneshop.service.impl.PaymentServiceImpl;
import vn.ute.smartphoneshop.utils.PriceUtil;
import vn.ute.smartphoneshop.utils.SecurityUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller("orderUserController")
@RequestMapping("/user/checkout")
public class OrderController {

    @Autowired
    IBrandService brandService;

    @Autowired
    IOrderService orderService;

    @Autowired
    private IUserService userService;

    @Autowired
    ICartService cartService;

    @Autowired
    ICartDetailService cartDetailService;

    @Autowired
    IVoucherService voucherService;

    @Autowired
    IProductService productService;

    @Autowired
    private PaymentServiceImpl paymentService;

    @Autowired
    private APIContext apiContext; // Thêm APIContext

    private UserDTO getCurrentUser() {
        String username = SecurityUtil.getCurrentUsername();
        return userService.findByUsername(username);
    }

    @GetMapping("")
    public String orders(Model model, @RequestParam(value = "selectedProducts", required = false)List<Integer> selectedProducts, HttpSession session) {
        if (selectedProducts == null|| selectedProducts.isEmpty()) {
            selectedProducts = (List<Integer>) session.getAttribute("selectedProducts");
        }
        else {
            session.setAttribute("selectedProducts", selectedProducts);
        }
        UserDTO currentUser = getCurrentUser();
        CartEntity cartEntity = new CartEntity();
        List<CartDetailRequest> cartDetailRequestList = new ArrayList<>();
        List<CartDetailRequest> cartDetailToBuy = new ArrayList<>();
        int numberProducts = 0;
        BigDecimal totalPriceToPayment = BigDecimal.ZERO;

        if (currentUser != null) {
            cartEntity = cartService.findCartByUserId(currentUser.getUserId());
            if (cartEntity != null) {
                cartDetailRequestList = cartDetailService.findByCartId(cartEntity.getCartId());
                numberProducts = cartDetailRequestList.size();
                if (cartDetailRequestList.size() > 2) {
                    cartDetailRequestList = cartDetailRequestList.subList(0, 2);
                }
            }
        }

        for (int productId : selectedProducts) {
            CartDetailEntity cartDetailEntity = cartDetailService.findByCartIdAndProductId(cartEntity.getCartId(),productId);
            CartDetailRequest cartDetailRequest = cartDetailService.convertCartDetailRequest(cartDetailEntity);
            cartDetailToBuy.add(cartDetailRequest);
            totalPriceToPayment = totalPriceToPayment.add(BigDecimal.valueOf(cartDetailRequest.getCartPrice()));
        }


        // Retrieve voucher names and values from service or database
        List<String> voucherNames = voucherService.getAllVoucherNames();
        List<Float> voucherValues = voucherService.getAllVoucherValues();

        List<BrandEntity> brandEntityList = brandService.findAll();
        model.addAttribute("user", currentUser);
        model.addAttribute("address", currentUser.getAddress());
        model.addAttribute("numberProducts", numberProducts);
        model.addAttribute("cart", cartEntity);
        model.addAttribute("cartDetailList", cartDetailRequestList);
        model.addAttribute("brands", brandEntityList);
        model.addAttribute("cartDetailToBuy", cartDetailToBuy);
        model.addAttribute("totalPriceToPayment",totalPriceToPayment);

        // Add voucher info to model to be used in JavaScript
        model.addAttribute("voucherNames", voucherNames);
        model.addAttribute("voucherValues", voucherValues);

        session.setAttribute("cartDetailToBuy", cartDetailToBuy);
        session.setAttribute("totalPriceToPayment", totalPriceToPayment);

        return "web/checkout";
    }

    @PostMapping("/create-order")
    public String createOrder(@RequestParam(value = "voucherCode", required = false) String voucherCode,
                              @RequestParam("payment-method") String paymentMethod,
                              HttpSession session, HttpServletResponse response,
                              @SessionAttribute("selectedProducts") List<Integer> selectedProducts,
                              RedirectAttributes redirectAttributes) {

        UserDTO currentUser = getCurrentUser();

        CartEntity cart = cartService.findCartByUserId(currentUser.getUserId());
        List<CartDetailRequest> cartDetailToBuy = (List<CartDetailRequest>) session.getAttribute("cartDetailToBuy");
        BigDecimal totalPriceToPayment = (BigDecimal) session.getAttribute("totalPriceToPayment");

        VoucherEntity voucher = null;
        BigDecimal discount = BigDecimal.ZERO;

        if (voucherCode != null && !voucherCode.isEmpty()) {
            voucher = voucherService.findVoucherByCode(voucherCode);
            if (voucher != null) {
                BigDecimal discountPercent = BigDecimal.valueOf(voucher.getDiscountPercent());
                discount = totalPriceToPayment.multiply(discountPercent.divide(BigDecimal.valueOf(100)));

                totalPriceToPayment = totalPriceToPayment.subtract(discount);
                session.removeAttribute("totalPriceToPayment");
                session.setAttribute("totalPriceToPayment", totalPriceToPayment);
                session.setAttribute("voucher",voucher);
            }
        }

        PaymentEntity payment = paymentService.findPaymentMethod(paymentMethod);

        if ("Paypal".equalsIgnoreCase(paymentMethod)) {
            try {
                BigDecimal totalPriceUSD = PriceUtil.convertVNDToUSD(totalPriceToPayment);
                String approvalUrl = createPayPalPayment(totalPriceUSD, currentUser, cart, cartDetailToBuy, voucher, payment, session);
                response.sendRedirect(approvalUrl);
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                redirectAttributes.addAttribute("selectedProducts", selectedProducts);
                return "redirect:/user/checkout";
            }
        }
        else if("VnPay".equalsIgnoreCase(paymentMethod)){
            session.setAttribute("totalPriceToPayment", totalPriceToPayment);
            session.setAttribute("payment", payment);
            return "redirect:/user/checkout/vnpay";
        }
        else {
            OrderEntity order = orderService.createOrder(currentUser.getUserId(), totalPriceToPayment, voucher, payment, cart.getCartId(), cartDetailToBuy);
            return "redirect:/user/my-profile";
        }
    }

    public String createPayPalPayment(BigDecimal amount, UserDTO user, CartEntity cart, List<CartDetailRequest> cartDetails, VoucherEntity voucher, PaymentEntity paymentMethod, HttpSession session) throws PayPalRESTException {
        // Tính tổng các mục
        BigDecimal itemsTotal = BigDecimal.ZERO;
        List<Item> items = new ArrayList<>();
        for (CartDetailRequest cartDetail : cartDetails) {
            BigDecimal itemPriceUSD = PriceUtil.convertVNDToUSD(BigDecimal.valueOf(cartDetail.getProductId().getPrice())).setScale(2, RoundingMode.HALF_UP);
            BigDecimal itemTotal = itemPriceUSD.multiply(BigDecimal.valueOf(cartDetail.getQuantity())).setScale(2, RoundingMode.HALF_UP);
            itemsTotal = itemsTotal.add(itemTotal);

            Item item = new Item();
            item.setName(cartDetail.getProductId().getName());
            item.setCurrency("USD");
            item.setPrice(String.format("%.2f", itemPriceUSD));
            item.setQuantity(String.valueOf(cartDetail.getQuantity()));
            items.add(item);
        }

        // Tính chiết khấu (nếu có)
        BigDecimal discountAmount = BigDecimal.ZERO;
        if (voucher != null) {
            BigDecimal discountPercent = BigDecimal.valueOf(voucher.getDiscountPercent());
            discountAmount = itemsTotal.multiply(discountPercent).divide(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP);
        }

        // Tính tổng sau chiết khấu
        BigDecimal totalAfterDiscount = itemsTotal.subtract(discountAmount).setScale(2, RoundingMode.HALF_UP);

        // Tạo đối tượng số tiền
        Amount paypalAmount = new Amount();
        paypalAmount.setCurrency("USD");
        paypalAmount.setTotal(String.format("%.2f", totalAfterDiscount));

        // Thiết lập chi tiết số tiền
        Details details = new Details();
        details.setSubtotal(String.format("%.2f", itemsTotal));
        if (voucher != null) {
            details.setShippingDiscount(String.format("%.2f", discountAmount));
        }
        paypalAmount.setDetails(details);

        // Tạo danh sách các mục thanh toán
        ItemList itemList = new ItemList();
        itemList.setItems(items);

        // Tạo giao dịch
        Transaction transaction = new Transaction();
        transaction.setAmount(paypalAmount);
        transaction.setDescription("Order payment for user checkout");
        transaction.setItemList(itemList);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        // Tạo đối tượng người thanh toán
        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        // Tạo đối tượng thanh toán
        Payment payment = new Payment();
        payment.setIntent("sale"); // Loại giao dịch: "sale"
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        // Đặt URL chuyển hướng khi thành công hoặc hủy, sử dụng các biến cấu hình
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl("http://localhost:8080/user/checkout/paypal/cancel"); // URL khi hủy thanh toán
        redirectUrls.setReturnUrl("http://localhost:8080/user/checkout/paypal/success"); // URL khi thanh toán thành công
        payment.setRedirectUrls(redirectUrls);

        // Tạo giao dịch qua PayPal
        Payment createdPayment = payment.create(apiContext);

        // Lưu thông tin đơn hàng vào session để xử lý sau khi thanh toán thành công
        session.setAttribute("currentUser", user);
        session.setAttribute("cart", cart);
        session.setAttribute("cartDetails", cartDetails);
        session.setAttribute("voucher", voucher);
        session.setAttribute("paymentMethod", paymentMethod);
        session.setAttribute("paypalPaymentId", createdPayment.getId());
        // Lấy URL phê duyệt từ danh sách liên kết
        for (Links link : createdPayment.getLinks()) {
            if (link.getRel().equalsIgnoreCase("approval_url")) {
                return link.getHref(); // Trả về URL để chuyển hướng người dùng
            }
        }

        // Nếu không tìm thấy URL phê duyệt
        throw new PayPalRESTException("Approval URL not found");
    }


    /**
     * Xử lý khi thanh toán thành công
     */
    @GetMapping("/paypal/success")
    public String paypalSuccess(@RequestParam("paymentId") String paymentId,
                                @RequestParam("PayerID") String payerId,
                                HttpSession session,
                                @SessionAttribute("selectedProducts") List<Integer> selectedProducts,
                                RedirectAttributes redirectAttributes) {
        try {
            // Lấy thông tin thanh toán từ PayPal
            Payment payment = Payment.get(apiContext, paymentId);

            // Thực hiện giao dịch
            PaymentExecution paymentExecution = new PaymentExecution();
            paymentExecution.setPayerId(payerId);
            Payment executedPayment = payment.execute(apiContext, paymentExecution);

            // Kiểm tra trạng thái thanh toán
            if ("approved".equalsIgnoreCase(executedPayment.getState())) {
                // Lấy thông tin từ session
                UserDTO user = (UserDTO) session.getAttribute("currentUser");
                CartEntity cart = (CartEntity) session.getAttribute("cart");
                List<CartDetailRequest> cartDetails = (List<CartDetailRequest>) session.getAttribute("cartDetails");
                VoucherEntity voucher = (VoucherEntity) session.getAttribute("voucher");
                PaymentEntity paymentMethod = (PaymentEntity) session.getAttribute("paymentMethod");
                BigDecimal cartTotalPrice = (BigDecimal) session.getAttribute("cartTotalPrice"); // Lấy tổng giá đã giảm

                if (user == null || cart == null || cartDetails == null || paymentMethod == null || cartTotalPrice == null) {
                    redirectAttributes.addAttribute("selectedProducts", selectedProducts);
                    return "redirect:/user/checkout";
                }

                // Tạo đơn hàng với tổng giá đã giảm
                OrderEntity order = orderService.createOrder(user.getUserId(), cartTotalPrice, voucher, paymentMethod, cart.getCartId(), cartDetails);

                // Xóa các thuộc tính trong session sau khi đã xử lý xong
                session.removeAttribute("currentUser");
                session.removeAttribute("cart");
                session.removeAttribute("cartDetails");
                session.removeAttribute("voucher");
                session.removeAttribute("paymentMethod");
                session.removeAttribute("paypalPaymentId");
                session.removeAttribute("cartTotalPrice"); // Xóa tổng giá sau giảm

                return "redirect:/user/my-profile";
            } else {
                // Thanh toán không thành công, chuyển hướng quay lại trang checkout
                redirectAttributes.addAttribute("selectedProducts", selectedProducts);
                return "redirect:/user/checkout";
            }
        } catch (PayPalRESTException e) {
            e.printStackTrace();
            redirectAttributes.addAttribute("selectedProducts", selectedProducts);
            return "redirect:/user/checkout"; // Quay lại trang checkout nếu có lỗi
        }
    }

    /**
     * Xử lý khi thanh toán bị hủy
     */
    @GetMapping("/paypal/cancel")
    public String paypalCancel(@SessionAttribute("selectedProducts") List<Integer> selectedProducts, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("selectedProducts", selectedProducts);
        return "redirect:/user/checkout"; // Quay lại trang checkout
    }
}



