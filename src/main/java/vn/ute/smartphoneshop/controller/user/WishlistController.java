package vn.ute.smartphoneshop.controller.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.ute.smartphoneshop.entity.BrandEntity;
import vn.ute.smartphoneshop.entity.CartDetailEntity;
import vn.ute.smartphoneshop.entity.CartEntity;
import vn.ute.smartphoneshop.entity.WishlistEntity;
import vn.ute.smartphoneshop.model.dto.*;
import vn.ute.smartphoneshop.model.request.CartDetailRequest;
import vn.ute.smartphoneshop.service.*;
import vn.ute.smartphoneshop.utils.SecurityUtil;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/user/wishlists")
public class WishlistController {
    @Autowired
    ICartService cartService;

    @Autowired
    ICartDetailService cartDetailService;

    @Autowired
    IUserService userService;

    @Autowired
    IProductService productService;

    @Autowired
    IWishlistService wishlistService;

    @Autowired
    IBrandService brandService;

    private UserDTO getCurrentUser() {
        String username = SecurityUtil.getCurrentUsername();
        return userService.findByUsername(username);
    }

    @GetMapping("")
    public String showWishList(Model model, HttpSession session) {
        CartEntity cartEntity = new CartEntity();
        List<CartDetailRequest> cartDetailList = new ArrayList<>();
        List<CartDetailRequest> cartDetailListFull = new ArrayList<>();
        int numberProducts = 0;

        List<WishlistDTO> wishlistDTOList = new ArrayList<>();

        UserDTO user = getCurrentUser();

        if (user != null) {
            cartEntity = cartService.findCartByUserId(user.getUserId());
            wishlistDTOList = wishlistService.getWishlistsByUserId(user.getUserId());
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
        model.addAttribute("wishlistDTOList", wishlistDTOList);

        session.setAttribute("cart", cartEntity);
        session.setAttribute("cartDetailList", cartDetailList);
        session.setAttribute("cartDetailListFull", cartDetailListFull);
        session.setAttribute("numberProducts", numberProducts);
        session.setAttribute("user", user);

        return "web/wishlist";
    }

    @GetMapping("/add-wishlist")
    public String addProduct(@Valid @RequestParam("id") int productId, Model model, HttpSession session, HttpServletRequest request) {
        UserDTO userDTO = (UserDTO) session.getAttribute("user");

        WishlistDTO wishlistDTO = wishlistService.findByUserIdAndProductId(userDTO.getUserId(), productId);
        ProductDTO productDTO = productService.findProductById(productId);

        if (wishlistDTO != null) {
            String error = "Exist product in wishlist";
            model.addAttribute("error", error);
        }
        else {
            if (productDTO.getStockQuantity() == 0){
                String error = "Not enough stock in product";
                model.addAttribute("error", error);
            }
            else {
                if(!wishlistService.addWishlist(new WishlistDTO(userDTO.getUserId(),productDTO))){
                    String error = "Could not add wishlist";
                    model.addAttribute("error", error);
                }
            }
        }
        return "redirect:"+request.getHeader("referer");
    }

    @GetMapping("/delete-wishlist")
    public String deleteProduct(@Valid@RequestParam("productId") int productId, Model model, HttpSession session) {
        UserDTO userDTO = (UserDTO) session.getAttribute("user");
            WishlistDTO wishlistDTO = wishlistService.findByUserIdAndProductId(userDTO.getUserId(),productId);
            if(wishlistService.deleteWishlist(wishlistDTO)){
                return "redirect:/user/wishlists";
            }

        return "redirect:/user/wishlists";
    }
}
