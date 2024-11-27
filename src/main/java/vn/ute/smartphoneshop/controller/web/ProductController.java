package vn.ute.smartphoneshop.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import vn.ute.smartphoneshop.entity.BrandEntity;
import vn.ute.smartphoneshop.entity.CartDetailEntity;
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
import java.util.stream.Collectors;

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

//    @GetMapping("")
//    public String index(@RequestParam Map<String, Object> params, Model model) {
//        List<ProductDTO> productList;
//
//        // Check if there are any search parameters
//        if (params == null || params.isEmpty()) {
//            // If no search parameters, fetch all products
//            productList = productService.findAllProduct();
//        } else {
//            // If there are search parameters, filter products using the params map
//            productList = productService.findAll(params);
//        }
//
//        List<BrandEntity> brandEntityList = brandService.findAll();
//
//        // Handle cart retrieval logic
//        CartEntity cartEntity = new CartEntity();
//        List<CartDetailRequest> cartDetailRequestList = new ArrayList<>();
//        int numberProducts = cartDetailRequestList.size();
//
//        if (getCurrentUser() != null) {
//            cartEntity = cartService.findCartByUserId(getCurrentUser().getUserId());
//            if (cartEntity != null) {
//                cartDetailRequestList = cartDetailService.findByCartId(cartEntity.getCartId());
//                numberProducts= cartDetailRequestList.size();
//                if(cartDetailRequestList.size() > 2){
//                    cartDetailRequestList = cartDetailRequestList.subList(0, 2);
//                }
//            }
//        }
//
//        // Add attributes to the model
//        model.addAttribute("numberProducts", numberProducts);
//        model.addAttribute("cart", cartEntity);
//        model.addAttribute("cartDetailList", cartDetailRequestList);
////        model.addAttribute("products", productList);
//        model.addAttribute("brands", brandEntityList);
//        return "web/shop-left-sidebar";
//    }

    @GetMapping("")
    public String index(@RequestParam Map<String, Object> params,
                        @RequestParam(defaultValue = "0") int page, // Trang hiện tại (bắt đầu từ 0)
                        @RequestParam(defaultValue = "9") int size,
                        Model model) {
        Pageable pageable = PageRequest.of(page, size);

        Page<ProductDTO> productPage = params != null && !params.isEmpty()
                ? productService.findAll(params, pageable)
                : productService.findAllProduct(pageable);

        // Tính toán vị trí sản phẩm hiển thị
        int totalItems = (int) productPage.getTotalElements(); // Tổng số sản phẩm
        int startItem = page * size + 1; // Sản phẩm đầu tiên
        int endItem = Math.min(startItem + size - 1, totalItems); // Sản phẩm cuối cùng

        String showingInfo = String.format("Showing : %02d-%02d of %d", startItem, endItem, totalItems);

        // Xử lý queryParams
        String queryParams = params.entrySet().stream()
                .filter(entry -> !"page".equals(entry.getKey()) && !"size".equals(entry.getKey())) // Loại bỏ page và size
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));

        String baseUrl = "/products" + (queryParams.isEmpty() ? "?" : "?" + queryParams + "&");
        model.addAttribute("baseUrl", baseUrl);

        List<BrandEntity> brandEntityList = brandService.findAll();

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

        model.addAttribute("numberProducts", numberProducts);
        model.addAttribute("cart", cartEntity);
        model.addAttribute("cartDetailList", cartDetailRequestList);
        model.addAttribute("products", productPage.getContent());
        model.addAttribute("brands", brandEntityList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("showingInfo", showingInfo);
        model.addAttribute("size", size);

        return "web/shop-left-sidebar";
    }



    @GetMapping("/{id}")
    public String show(@PathVariable("id") Integer id, Model model) {
        List<BrandEntity> brandEntityList = brandService.findAll();
        ProductDTO productDTO = productService.findProductById(id);

        List<ProductDTO> productDTOList = productService.findProductByBrandName(productDTO.getBrandName());
        if (productDTOList.size() > 3){
            productDTOList = productDTOList.subList(0, 3);
        }

        int amountOfProduct = 0;

        CartEntity cartEntity = new CartEntity();
        List<CartDetailRequest> cartDetailRequestList = new ArrayList<>();
        int numberProducts = cartDetailRequestList.size();

        if (getCurrentUser() != null) {
            cartEntity = cartService.findCartByUserId(getCurrentUser().getUserId());
            if(cartEntity != null){
                cartDetailRequestList = cartDetailService.findByCartId(cartEntity.getCartId());
                CartDetailEntity cartDetail = cartDetailService.findByCartIdAndProductId(cartEntity.getCartId(),id);
                if (cartDetail != null) {
                    amountOfProduct = cartDetail.getQuantity();
                }
                numberProducts= cartDetailRequestList.size();
                if(cartDetailRequestList.size() > 2){
                    cartDetailRequestList = cartDetailRequestList.subList(0, 2);
                }
            }
        }

        model.addAttribute("numberProducts", numberProducts);
        model.addAttribute("cart", cartEntity);
        model.addAttribute("cartDetailList", cartDetailRequestList);
        model.addAttribute("products", productDTOList);
        model.addAttribute("amountOfProduct", amountOfProduct);

        model.addAttribute("product", productDTO);
        model.addAttribute("brands", brandEntityList);
        return "web/single-product-left-sidebar";
    }
}
