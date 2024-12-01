package vn.ute.smartphoneshop.controller.web;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.ute.smartphoneshop.entity.*;
import vn.ute.smartphoneshop.model.dto.ProductDTO;
import vn.ute.smartphoneshop.model.dto.RatingDTO;
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

    @Autowired
    IRatingService ratingService;

    private UserDTO getCurrentUser() {
        String username = SecurityUtil.getCurrentUsername();
        return userService.findByUsername(username);
    }


    @GetMapping("")
    public String index(@RequestParam Map<String, Object> params,
                        @RequestParam(defaultValue = "0") int page,  // Trang hiện tại (bắt đầu từ 0)
                        @RequestParam(defaultValue = "9") int size,  // Số lượng sản phẩm mỗi trang
                        @RequestParam(defaultValue = "asc") String sortOrder,  // Sắp xếp theo thứ tự (asc/desc)
                        @RequestParam(defaultValue = "price") String sortBy,  // Sắp xếp theo thuộc tính
                        Model model) {

        // Tạo Sort (asc/desc) cho Pageable
        Sort sort = sortOrder.equals("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        // Tạo Pageable từ page, size và sort
        Pageable pageable = PageRequest.of(page, size, sort);

        // Kiểm tra xem có tham số lọc không, nếu có thì lọc theo các tham số trong params
        Page<ProductDTO> productPage = params != null && !params.isEmpty()
                ? productService.findAll(params, pageable)
                : productService.findAllProduct(pageable);

        // Tính toán vị trí sản phẩm hiển thị
        int totalItems = (int) productPage.getTotalElements();  // Tổng số sản phẩm
        int startItem = page * size + 1;  // Sản phẩm đầu tiên
        int endItem = Math.min(startItem + size - 1, totalItems);  // Sản phẩm cuối cùng

        // Chuỗi hiển thị thông tin về trang
        String showingInfo = String.format("Showing: %02d-%02d of %d", startItem, endItem, totalItems);

        // Xử lý queryParams: Giữ lại các tham số lọc (như name, price, brandName...) khi chuyển trang hoặc sắp xếp
        String queryParams = params.entrySet().stream()
                .filter(entry -> !"page".equals(entry.getKey()) && !"size".equals(entry.getKey()) && !"sortBy".equals(entry.getKey()) && !"sortOrder".equals(entry.getKey()))  // Loại bỏ page, size, sortBy, sortOrder
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));

        // Base URL cho phân trang và sắp xếp
        String baseUrl = "/products" + (queryParams.isEmpty() ? "?" : "?" + queryParams + "&");
        model.addAttribute("baseUrl", baseUrl);

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

        // Các thuộc tính khác cần truyền vào view
        List<BrandEntity> brandEntityList = brandService.findAll();
        model.addAttribute("numberProducts", numberProducts);
        model.addAttribute("cart", cartEntity);
        model.addAttribute("cartDetailList", cartDetailRequestList);
        model.addAttribute("products", productPage.getContent());
        model.addAttribute("brands", brandEntityList);
        model.addAttribute("showingInfo", showingInfo);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortOrder", sortOrder);
        model.addAttribute("size", size);

        return "web/shop-left-sidebar";  // View tương ứng
    }


    @GetMapping("/{id}")
    public String show(@PathVariable("id") Integer id, Model model) {
        List<BrandEntity> brandEntityList = brandService.findAll();
        ProductDTO productDTO = productService.findProductById(id);

        List<ProductDTO> productDTOList = productService.findProductByBrandName(productDTO.getBrandName());
        if (productDTOList.size() > 3) {
            productDTOList = productDTOList.subList(0, 3);
        }

        int amountOfProduct = 0;

        CartEntity cartEntity = new CartEntity();
        List<CartDetailRequest> cartDetailRequestList = new ArrayList<>();
        int numberProducts = cartDetailRequestList.size();

        if (getCurrentUser() != null) {
            cartEntity = cartService.findCartByUserId(getCurrentUser().getUserId());
            if (cartEntity != null) {
                cartDetailRequestList = cartDetailService.findByCartId(cartEntity.getCartId());
                CartDetailEntity cartDetail = cartDetailService.findByCartIdAndProductId(cartEntity.getCartId(), id);
                if (cartDetail != null) {
                    amountOfProduct = cartDetail.getQuantity();
                }
                numberProducts = cartDetailRequestList.size();
                if (cartDetailRequestList.size() > 2) {
                    cartDetailRequestList = cartDetailRequestList.subList(0, 2);
                }
            }
        }

        List<RatingEntity> ratings = ratingService.findByProductId(id);
        float ratingCount = ratingService.countRatingStar(id);
        int ratingUser = ratingService.countUser(id);

        model.addAttribute("numberProducts", numberProducts);
        model.addAttribute("cart", cartEntity);
        model.addAttribute("cartDetailList", cartDetailRequestList);
        model.addAttribute("products", productDTOList);
        model.addAttribute("amountOfProduct", amountOfProduct);

        model.addAttribute("product", productDTO);
        model.addAttribute("brands", brandEntityList);

        model.addAttribute("ratings", ratings);
        model.addAttribute("ratingCount", ratingCount);
        model.addAttribute("ratingUser", ratingUser);
        model.addAttribute("rating",new RatingDTO());
        return "web/single-product-left-sidebar";
    }

    @PostMapping("/reviews")
    public String reviews(@Valid @ModelAttribute("rating") RatingDTO ratingDTO,
                          @RequestParam("productId") int productId, HttpSession session,
                          RedirectAttributes redirectAttributes) {
        if (!SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser")){
            UserDTO user = (UserDTO) session.getAttribute("user");
            ratingDTO.setProductId(productId);
            ratingDTO.setUserId(user.getUserId());

            if(ratingService.checkOrderFirst(productId,user.getUserId())){
                if (!ratingService.insert(ratingDTO)){
                    String msg = "Not found user/product";
                    redirectAttributes.addFlashAttribute("msg", msg);
                }
            }
            else {
                String msg = "You need to buy first";
                redirectAttributes.addFlashAttribute("msg", msg);
            }
        }
        return "redirect:/products/"+productId;
    }
}
