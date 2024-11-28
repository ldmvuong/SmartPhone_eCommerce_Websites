package vn.ute.smartphoneshop.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.ute.smartphoneshop.entity.*;
import vn.ute.smartphoneshop.model.dto.UserDTO;
import vn.ute.smartphoneshop.model.request.CartDetailRequest;
import vn.ute.smartphoneshop.service.*;
import vn.ute.smartphoneshop.service.impl.PaymentServiceImpl;
import vn.ute.smartphoneshop.utils.SecurityUtil;

import java.math.BigDecimal;
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

    private UserDTO getCurrentUser() {
        String username = SecurityUtil.getCurrentUsername();
        return userService.findByUsername(username);
    }

    @GetMapping("")
    public String orders(Model model) {
        UserDTO currentUser = getCurrentUser();
        CartEntity cartEntity = new CartEntity();
        List<CartDetailRequest> cartDetailRequestList = new ArrayList<>();
        List<CartDetailRequest> cartDetail = new ArrayList<>();
        int numberProducts = 0;

        if (getCurrentUser() != null) {
            cartEntity = cartService.findCartByUserId(getCurrentUser().getUserId());
            if (cartEntity != null) {
                cartDetailRequestList = cartDetailService.findByCartId(cartEntity.getCartId());
                cartDetail = cartDetailRequestList;
                numberProducts = cartDetailRequestList.size();
                if (cartDetailRequestList.size() > 2) {
                    cartDetailRequestList = cartDetailRequestList.subList(0, 2);
                }
            }
        }

        // Retrieve voucher names and values from service or database
        List<String> voucherNames = voucherService.getAllVoucherNames();
        List<Float> voucherValues = voucherService.getAllVoucherValues();

        List<BrandEntity> brandEntityList = brandService.findAll();
        model.addAttribute("user", currentUser);
        model.addAttribute("address", currentUser.getAddress());
        model.addAttribute("numberProducts", numberProducts);
        model.addAttribute("cart", cartEntity);
        model.addAttribute("cartDetail", cartDetail);
        model.addAttribute("cartDetailList", cartDetailRequestList);
        model.addAttribute("brands", brandEntityList);

        // Add voucher info to model to be used in JavaScript
        model.addAttribute("voucherNames", voucherNames);
        model.addAttribute("voucherValues", voucherValues);

        return "web/checkout";
    }

    @PostMapping("/create-order")
    public String createOrder(@RequestParam( value = "voucherCode", required = false) String voucherCode,
                              @RequestParam("payment-method") String paymentMethod) {

        UserDTO currentUser = getCurrentUser();

        CartEntity cart = cartService.findCartByUserId(currentUser.getUserId());
        List<CartDetailRequest> cartDetailList = cartDetailService.findByCartId(cart.getCartId());

        BigDecimal cartTotalPrice = new BigDecimal(cart.getTotalPrice()); // Total price of the cart

        VoucherEntity voucher = null;
        BigDecimal discount = BigDecimal.ZERO;

        if (voucherCode != null && !voucherCode.isEmpty()) {
            voucher = voucherService.findVoucherByCode(voucherCode);
            if (voucher != null) {
                // Convert float to BigDecimal to perform the division
                BigDecimal discountPercent = BigDecimal.valueOf(voucher.getDiscountPercent());

                // Calculate the discount as BigDecimal
                discount = cartTotalPrice.multiply(discountPercent.divide(BigDecimal.valueOf(100)));

                // Update total price after discount
                cartTotalPrice = cartTotalPrice.subtract(discount);
            }
        }

        // Get payment method based on the selected payment method
        PaymentEntity payment = paymentService.findPaymentMethod(paymentMethod);

        // Create the order using service
        OrderEntity order = orderService.createOrder(currentUser.getUserId(), cartTotalPrice, voucher, payment, cart.getCartId(), cartDetailList);

        return "redirect:/home";
    }
}



