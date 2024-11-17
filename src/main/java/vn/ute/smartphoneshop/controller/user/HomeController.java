package vn.ute.smartphoneshop.controller.user;

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
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = (principal instanceof UserDetails) ? ((UserDetails) principal).getUsername() : principal.toString();
        return userService.findByUsername(username);
    }

    @GetMapping("/home")
    public String index(Model model) {
        List<BrandEntity> brandEntities = brandService.findAll();

        List<ProductEntity> newArrivalProductList = productService.newArrivalProduct();
        if (newArrivalProductList.size() > 12){
            newArrivalProductList = newArrivalProductList.subList(0, 12);
        }

        CartEntity cartEntity = cartService.findCartByUserId(getCurrentUser().getUserId());
        List<CartDetailRequest> cartDetailRequestList = new ArrayList<>();
        if(cartEntity != null){
            cartDetailRequestList = cartDetailService.findByCartId(cartEntity.getCartId());
        }

        model.addAttribute("cart", cartEntity);
        model.addAttribute("cartDetailList", cartDetailRequestList);
        model.addAttribute("newArrivalProducts", newArrivalProductList);
        model.addAttribute("brands", brandEntities);

        return "web/index";
    }
}
