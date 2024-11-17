package vn.ute.smartphoneshop.controller.user;

import jakarta.validation.Valid;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.ute.smartphoneshop.entity.CartDetailEntity;
import vn.ute.smartphoneshop.entity.CartEntity;
import vn.ute.smartphoneshop.entity.UserEntity;
import vn.ute.smartphoneshop.model.dto.CartDTO;
import vn.ute.smartphoneshop.model.dto.CartDetailDTO;
import vn.ute.smartphoneshop.model.dto.ProductDTO;
import vn.ute.smartphoneshop.model.dto.UserDTO;
import vn.ute.smartphoneshop.repository.UserRepository;
import vn.ute.smartphoneshop.service.ICartDetailService;
import vn.ute.smartphoneshop.service.ICartService;
import vn.ute.smartphoneshop.service.IProductService;
import vn.ute.smartphoneshop.service.IUserService;
import vn.ute.smartphoneshop.utils.SecurityUtil;

import java.math.BigDecimal;

@Controller
@RequestMapping("/user/carts")
public class CartController {
    @Autowired
    ICartService cartService;

    @Autowired
    ICartDetailService cartDetailService;

    @Autowired
    IUserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    IProductService productService;

    private UserDTO getCurrentUser() {
        String username = SecurityUtil.getCurrentUsername();
        return userService.findByUsername(username);
    }

    @GetMapping("/add-cart")
    public String addProduct(@Valid@RequestParam("id") int productId, @RequestParam("quantity") int quantity, Model model) {
        CartEntity cartEntity = cartService.findCartByUserId(getCurrentUser().getUserId());
        if (cartEntity == null) {
            CartDTO cartDTO = new CartDTO();
            UserEntity userEntity = userRepository.findByUserName(getCurrentUser().getUserName()).get();
            cartDTO.setUserId(userEntity.getUserId());
            cartDTO.setTotalPrice(0L);
            cartService.createCart(cartDTO);
        }
        if (cartEntity != null) {
            CartDetailEntity cartDetailEntity = cartDetailService.findByCartIdAndProductId(cartEntity.getCartId(), productId);
            ProductDTO productDTO = productService.findProductById(productId);
            if (cartDetailEntity != null) {
                cartDetailEntity.setQuantity(quantity+cartDetailEntity.getQuantity());
                Long price = cartEntity.getTotalPrice() - cartDetailEntity.getCartPrice() + productDTO.getPrice()*cartDetailEntity.getQuantity();
                cartDetailEntity.setCartPrice(productDTO.getPrice()*cartDetailEntity.getQuantity());
                cartEntity.setTotalPrice(price);
                cartService.updateCart(cartEntity);
                cartDetailService.update(cartDetailEntity);
            }
            else {
                CartDetailDTO cartDetailDTO = new CartDetailDTO();
                productDTO = productService.findProductById(productId);
                cartDetailDTO.setCartId(cartEntity.getCartId());
                cartDetailDTO.setProductId(productId);
                cartDetailDTO.setQuantity(quantity);
                cartDetailDTO.setCartPrice(productDTO.getPrice()*quantity);
                if (cartDetailService.insert(cartDetailDTO)) {
                    Long price = cartEntity.getTotalPrice();
                    cartEntity.setTotalPrice(price+cartDetailDTO.getCartPrice());
                    cartService.updateCart(cartEntity);
                    return "redirect:/home";
                }
            }
        }
        return "redirect:/home";
    }

    @GetMapping("/delete-cart")
    public String deleteProduct(@Valid@RequestParam("cartId") int cartId,@RequestParam("productId") int productId, Model model) {
        CartEntity cartEntity = cartService.findCartByUserId(getCurrentUser().getUserId());
        CartDetailEntity cartDetailEntity = cartDetailService.findByCartIdAndProductId(cartId,productId);
        if (cartEntity != null) {
            if(cartDetailService.delete(cartDetailEntity.getCartDetailId())){
                Long price = cartEntity.getTotalPrice();
                cartEntity.setTotalPrice(price-cartDetailEntity.getCartPrice());
                cartService.updateCart(cartEntity);
                return "redirect:/home";
            }
        }
        return "redirect:/home";
    }
}
