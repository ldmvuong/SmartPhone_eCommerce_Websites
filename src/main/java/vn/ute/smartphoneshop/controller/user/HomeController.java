package vn.ute.smartphoneshop.controller.user;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.ute.smartphoneshop.entity.BrandEntity;
import vn.ute.smartphoneshop.entity.CartEntity;
import vn.ute.smartphoneshop.entity.ProductEntity;
import vn.ute.smartphoneshop.model.dto.CartDTO;
import vn.ute.smartphoneshop.model.dto.CartDetailDTO;
import vn.ute.smartphoneshop.model.dto.UserDTO;
import vn.ute.smartphoneshop.model.request.CartDetailRequest;
import vn.ute.smartphoneshop.repository.CartRepository;
import vn.ute.smartphoneshop.service.*;
import vn.ute.smartphoneshop.utils.SecurityUtil;

import java.util.ArrayList;
import java.util.List;

@Controller("userHomeController")
public class HomeController {
    @Autowired
    IBrandService brandService;

    @Autowired
    IProductService productService;

    @Autowired
    IUserService userService;

    @Autowired
    ICartDetailService cartDetailService;

    @Autowired
    ICartService cartService;

    private UserDTO getCurrentUser() {
        String username = SecurityUtil.getCurrentUsername();
        return userService.findByUsername(username);
    }

    @GetMapping("/home")
    public String index(Model model, HttpSession session) {
        List<BrandEntity> brandEntities = brandService.findAll();
        UserDTO user = getCurrentUser();

        List<ProductEntity> newArrivalProductList = productService.newArrivalProduct();
        if (newArrivalProductList.size() > 12){
            newArrivalProductList = newArrivalProductList.subList(0, 12);
        }

        CartEntity cartEntity = new CartEntity();
        List<CartDetailRequest> cartDetailRequestList = new ArrayList<>();
        List<CartDetailRequest> cartDetailRequestListFull = new ArrayList<>();
        int numberProducts = cartDetailRequestList.size();

        if (user != null) {
            cartEntity = cartService.findCartByUserId(user.getUserId());
            if(cartEntity != null){
                cartDetailRequestList = cartDetailService.findByCartId(cartEntity.getCartId());
                cartDetailRequestListFull = cartDetailRequestList;
                numberProducts= cartDetailRequestList.size();
                if(cartDetailRequestList.size() > 2){
                    cartDetailRequestList = cartDetailRequestList.subList(0, 2);
                }
            }
        }

        model.addAttribute("numberProducts", numberProducts);
        model.addAttribute("cart", cartEntity);
        model.addAttribute("cartDetailList", cartDetailRequestList);
        model.addAttribute("newArrivalProducts", newArrivalProductList);
        model.addAttribute("brands", brandEntities);

        session.setAttribute("cart", cartEntity);
        session.setAttribute("cartDetailList", cartDetailRequestList);
        session.setAttribute("cartDetailListFull", cartDetailRequestListFull);
        session.setAttribute("user", user);
        session.setAttribute("numberProducts", numberProducts);

        return "web/index";
    }
}
