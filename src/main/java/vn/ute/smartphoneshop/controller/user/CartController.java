package vn.ute.smartphoneshop.controller.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.ute.smartphoneshop.entity.BrandEntity;
import vn.ute.smartphoneshop.entity.CartDetailEntity;
import vn.ute.smartphoneshop.entity.CartEntity;
import vn.ute.smartphoneshop.entity.UserEntity;
import vn.ute.smartphoneshop.model.dto.CartDTO;
import vn.ute.smartphoneshop.model.dto.CartDetailDTO;
import vn.ute.smartphoneshop.model.dto.ProductDTO;
import vn.ute.smartphoneshop.model.dto.UserDTO;
import vn.ute.smartphoneshop.model.request.CartDetailRequest;
import vn.ute.smartphoneshop.repository.UserRepository;
import vn.ute.smartphoneshop.service.*;
import vn.ute.smartphoneshop.utils.SecurityUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
    IProductService productService;

    @Autowired
    IBrandService brandService;

    private UserDTO getCurrentUser() {
        String username = SecurityUtil.getCurrentUsername();
        return userService.findByUsername(username);
    }

    @GetMapping("")
    public String showCarts(Model model, HttpSession session) {
        CartEntity cartEntity = new CartEntity();
        List<CartDetailRequest> cartDetailList = new ArrayList<>();
        List<CartDetailRequest> cartDetailListFull = new ArrayList<>();
        int numberProducts = 0;

        UserDTO user = getCurrentUser();

        if (user != null) {
            cartEntity = cartService.findCartByUserId(user.getUserId());
            if(cartEntity != null){
                cartDetailList = cartDetailService.findByCartId(cartEntity.getCartId());
                cartDetailListFull = cartDetailList;
                numberProducts= cartDetailListFull.size();
                if(cartDetailListFull.size() > 2){
                    cartDetailList = cartDetailList.subList(0, 2);
                }
            }
        }

        List<BrandEntity> brandEntities = brandService.findAll();

        model.addAttribute("brands", brandEntities);
        model.addAttribute("numberProducts", numberProducts);
        model.addAttribute("cart", cartEntity);
        model.addAttribute("cartDetailList", cartDetailList);
        model.addAttribute("cartDetailListFull", cartDetailListFull);

        session.setAttribute("cart", cartEntity);
        session.setAttribute("cartDetailList", cartDetailList);
        session.setAttribute("cartDetailListFull", cartDetailListFull);
        session.setAttribute("numberProducts", numberProducts);
        session.setAttribute("user", user);

        return "web/cart";
    }

    @GetMapping("/add-cart")
    public String addProduct(@Valid@RequestParam("id") int productId, @RequestParam("quantity") int quantity, Model model, HttpSession session, HttpServletRequest request) {
        CartEntity cartEntity = (CartEntity) session.getAttribute("cart");
        UserDTO userDTO = (UserDTO) session.getAttribute("user");

        if (cartEntity == null) {
            cartService.createCart(new CartDTO(userDTO.getUserId(),0L));
        }
        cartEntity = cartService.findCartByUserId(userDTO.getUserId());

        CartDetailEntity cartDetailEntity = cartDetailService.findByCartIdAndProductId(cartEntity.getCartId(), productId);;
        ProductDTO productDTO = productService.findProductById(productId);
        Long totalPrice, price;

        if (cartDetailEntity != null) {
            price = (cartDetailEntity.getQuantity()+quantity)*productDTO.getPrice();
            totalPrice = cartEntity.getTotalPrice() - cartDetailEntity.getCartPrice() + price;

            if (cartDetailEntity.getQuantity()> productDTO.getStockQuantity()) {
                String error = "Could not add quantity";
                model.addAttribute("error", error);
            }
            if (totalPrice > 1000000000){
                String error = "Could not add quantity";
                model.addAttribute("error", error);
            }

            if(!cartDetailService.update(new CartDetailDTO(cartEntity.getCartId(),productId,quantity+cartDetailEntity.getQuantity(),price)))
            {
                model.addAttribute("error", "Could not update quantity");
            }
        }
        else {
            price = quantity*productDTO.getPrice();
            if (cartDetailService.insert(new CartDetailDTO(cartEntity.getCartId(),
                    productId,quantity,
                    price))) {
                return "redirect:"+request.getHeader("referer");
            }
            else {
                model.addAttribute("error", "Could not add product");
            }
        }
        return "redirect:"+request.getHeader("referer");
    }

    @GetMapping("/delete-cart")
    public String deleteProduct(@Valid@RequestParam("productId") int productId, Model model, HttpSession session, HttpServletRequest request) {
        CartEntity cartEntity = (CartEntity) session.getAttribute("cart");
        if (cartEntity != null) {
            CartDetailEntity cartDetailEntity = cartDetailService.findByCartIdAndProductId(cartEntity.getCartId(),productId);
            if(cartDetailService.delete(cartDetailEntity.getCartDetailId())){
                return "redirect:"+request.getHeader("referer");
            }
        }
        return "redirect:"+request.getHeader("referer");
    }

    @GetMapping("/dec-cart")
    public String decrementCart(@Valid @RequestParam("productId") int productId, HttpSession session, RedirectAttributes redirectAttributes) {
        CartEntity cart = (CartEntity) session.getAttribute("cart");

        ProductDTO product = productService.findProductById(productId);
        CartDetailEntity cartDetail = cartDetailService.findByCartIdAndProductId(cart.getCartId(), productId);
        CartDetailDTO cartDetailDTO;
        long price;

        if(cartDetail != null) {
            if(cartDetail.getQuantity() > 1) {
                price = product.getPrice()*(cartDetail.getQuantity()-1);
                cartDetailDTO = new CartDetailDTO(cart.getCartId(),productId,cartDetail.getQuantity() - 1, price);
                if (!cartDetailService.update(cartDetailDTO)) {
                    String error = "Could not update cart detail";
                    redirectAttributes.addFlashAttribute("error", error);
                }
            }
            else {
                String error = "Quantity must be greater than 1";
                redirectAttributes.addFlashAttribute("error", error);
            }
        }
        return "redirect:/user/carts";
    }

    @GetMapping("/inc-cart")
    public String incrementCart(@Valid @RequestParam("productId") int productId, HttpSession session, RedirectAttributes redirectAttributes) {
        CartEntity cart = (CartEntity) session.getAttribute("cart");

        ProductDTO product = productService.findProductById(productId);
        CartDetailEntity cartDetail = cartDetailService.findByCartIdAndProductId(cart.getCartId(), productId);
        CartDetailDTO cartDetailDTO;
        long price;

        if(cartDetail != null) {
            price = product.getPrice()*(cartDetail.getQuantity()+1);
            cartDetailDTO = new CartDetailDTO(cart.getCartId(),productId,cartDetail.getQuantity() + 1, price);
            if (cartDetailDTO.getQuantity() > product.getStockQuantity()){
                String error = "Could not add quantity";
                redirectAttributes.addFlashAttribute("error", error);
            }
            if(price > 1000000000){
                String error = "The cart value in your cart has reached the limit.";
                redirectAttributes.addFlashAttribute("error", error);
            }
            else if (!cartDetailService.update(cartDetailDTO)) {
                String error = "Could not update cart detail";
                redirectAttributes.addFlashAttribute("error", error);
            }
        }
        return "redirect:/user/carts";
    }
}
