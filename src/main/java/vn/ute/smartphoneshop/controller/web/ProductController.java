package vn.ute.smartphoneshop.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import vn.ute.smartphoneshop.entity.BrandEntity;
import vn.ute.smartphoneshop.entity.CartEntity;
import vn.ute.smartphoneshop.entity.ProductEntity;
import vn.ute.smartphoneshop.model.dto.ProductDTO;
import vn.ute.smartphoneshop.model.dto.UserDTO;
import vn.ute.smartphoneshop.model.request.CartDetailRequest;
import vn.ute.smartphoneshop.service.*;
import vn.ute.smartphoneshop.utils.SecurityUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller("userProductController")
@RequestMapping("/products")
public class ProductController {
    @Autowired
    IProductService productService;

    @Autowired
    IBrandService brandService;

    @Autowired
    IUserService userService;

    @Autowired
    ICartService cartService;

    @Autowired
    ICartDetailService cartDetailService;

    private UserDTO getCurrentUser() {
        String username = SecurityUtil.getCurrentUsername();
        return userService.findByUsername(username);
    }

    @GetMapping("")
    public String index(@RequestParam Map<String, Object> params, Model model) {
        List<ProductDTO> productList;

        // Check if there are any search parameters
        if (params == null || params.isEmpty()) {
            // If no search parameters, fetch all products
            productList = productService.findAllProduct();
        } else {
            // If there are search parameters, filter products using the params map
            productList = productService.findAll(params);
        }

        List<BrandEntity> brandEntityList = brandService.findAll();

        // Handle cart retrieval logic
        CartEntity cartEntity = new CartEntity();
        List<CartDetailRequest> cartDetailRequestList = new ArrayList<>();
        int numberProducts = cartDetailRequestList.size();

        if (getCurrentUser() != null) {
            cartEntity = cartService.findCartByUserId(getCurrentUser().getUserId());
            if (cartEntity != null) {
                cartDetailRequestList = cartDetailService.findByCartId(cartEntity.getCartId());
                numberProducts= cartDetailRequestList.size();
                if(cartDetailRequestList.size() > 2){
                    cartDetailRequestList = cartDetailRequestList.subList(0, 2);
                }
            }
        }

        // Add attributes to the model
        model.addAttribute("numberProducts", numberProducts);
        model.addAttribute("cart", cartEntity);
        model.addAttribute("cartDetailList", cartDetailRequestList);
        model.addAttribute("products", productList);
        model.addAttribute("brands", brandEntityList);
        return "web/shop-left-sidebar";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") Integer id, Model model) {
        List<BrandEntity> brandEntityList = brandService.findAll();
        ProductDTO productDTO = productService.findProductById(id);

        CartEntity cartEntity = new CartEntity();
        List<CartDetailRequest> cartDetailRequestList = new ArrayList<>();
        int numberProducts = cartDetailRequestList.size();

        if (getCurrentUser() != null) {
            cartEntity = cartService.findCartByUserId(getCurrentUser().getUserId());
            if(cartEntity != null){
                cartDetailRequestList = cartDetailService.findByCartId(cartEntity.getCartId());
                numberProducts= cartDetailRequestList.size();
                if(cartDetailRequestList.size() > 2){
                    cartDetailRequestList = cartDetailRequestList.subList(0, 2);
                }
            }
        }

        model.addAttribute("numberProducts", numberProducts);
        model.addAttribute("cart", cartEntity);
        model.addAttribute("cartDetailList", cartDetailRequestList);

        model.addAttribute("product", productDTO);
        model.addAttribute("brands", brandEntityList);
        return "web/single-product-left-sidebar";
    }
}
