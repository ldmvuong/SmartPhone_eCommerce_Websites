package vn.ute.smartphoneshop.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.ute.smartphoneshop.entity.BrandEntity;
import vn.ute.smartphoneshop.entity.CartEntity;
import vn.ute.smartphoneshop.model.dto.UserDTO;
import vn.ute.smartphoneshop.model.request.CartDetailRequest;
import vn.ute.smartphoneshop.service.*;
import vn.ute.smartphoneshop.utils.SecurityUtil;

import java.util.ArrayList;
import java.util.List;

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

    private UserDTO getCurrentUser() {
        String username = SecurityUtil.getCurrentUsername();
        return userService.findByUsername(username);
    }

    @GetMapping("")
    public String orders(Model model) {

        CartEntity cartEntity = new CartEntity();
        List<CartDetailRequest> cartDetailRequestList = new ArrayList<>();
        int numberProducts = 0;

        if (getCurrentUser() != null) {
            cartEntity = cartService.findCartByUserId(getCurrentUser().getUserId());
            if (cartEntity != null) {
                cartDetailRequestList = cartDetailService.findByCartId(cartEntity.getCartId());
                numberProducts = cartDetailRequestList.size();
                if (cartDetailRequestList.size() > 2) {
                    cartDetailRequestList = cartDetailRequestList.subList(0, 2);
                }
            }
        }

        List<BrandEntity> brandEntityList = brandService.findAll();
        model.addAttribute("numberProducts", numberProducts);
        model.addAttribute("cart", cartEntity);
        model.addAttribute("cartDetailList", cartDetailRequestList);
        model.addAttribute("brands", brandEntityList);
        return "web/checkout";
    }
}


