package vn.ute.smartphoneshop.controller.admin;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.ute.smartphoneshop.model.dto.UserDTO;
import vn.ute.smartphoneshop.model.request.ChangePasswordRequest;
import vn.ute.smartphoneshop.model.request.ProfileUpdateRequest;
import vn.ute.smartphoneshop.model.response.CustomerSalesDTO;
import vn.ute.smartphoneshop.model.response.ProductSalesDTO;
import vn.ute.smartphoneshop.model.response.RatingRespone;
import vn.ute.smartphoneshop.service.IOrderService;
import vn.ute.smartphoneshop.service.IProductService;
import vn.ute.smartphoneshop.service.IRatingService;
import vn.ute.smartphoneshop.service.IUserService;
import vn.ute.smartphoneshop.utils.SecurityUtil;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;


@Controller("adminHomeController")
public class HomeController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IProductService productService;

    @Autowired
    private IRatingService ratingService;

    private UserDTO getCurrentUser() {
        String username = SecurityUtil.getCurrentUsername();
        return userService.findByUsername(username);
    }

    private ProfileUpdateRequest createProfileUpdateRequest(UserDTO user) {
        ProfileUpdateRequest profileUpdateRequest = new ProfileUpdateRequest();
        BeanUtils.copyProperties(user, profileUpdateRequest);
        return profileUpdateRequest;
    }


    @GetMapping("/admin/home")
    public String home(HttpSession session, Model model) {
        Long orderCount = orderService.countOrders();
        BigDecimal totalPrice = orderService.calculateTotalOrderValue();
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String price = currencyFormatter.format(totalPrice);
        Long userCount = userService.countUser();
        Long productCount = productService.countProduct();

        List<ProductSalesDTO> productSalesDTOS = orderService.getTop5BestSellingProducts();
        List<CustomerSalesDTO> customerSalesDTOS = orderService.getTop5Customers();
        List<RatingRespone>  ratingRespones = ratingService.findAllRating();


        model.addAttribute("ratingRespones", ratingRespones);
        model.addAttribute("customerSalesDTOS", customerSalesDTOS);
        model.addAttribute("productSalesDTOS", productSalesDTOS);
        model.addAttribute("orderCount", orderCount);
        model.addAttribute("totalPrice", price);
        model.addAttribute("userCount", userCount);
        model.addAttribute("productCount", productCount);
        return "admin/index";
    }

    @GetMapping("/admin/my-profile")
    public String myProfile(Model model) {
        UserDTO user = getCurrentUser();
        ProfileUpdateRequest profileUpdateRequest = createProfileUpdateRequest(user);
        model.addAttribute("profileUpdateRequest", profileUpdateRequest);
        model.addAttribute("changePasswordRequest", new ChangePasswordRequest());
        return "admin/my-profile";
    }


    @PostMapping("/admin/my-profile/update")
    public String updateProfile(
            @Valid @ModelAttribute("profileUpdateRequest") ProfileUpdateRequest profileUpdateRequest,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        model.addAttribute("changePasswordRequest", new ChangePasswordRequest());

        if (bindingResult.hasErrors()) {
            return "admin/my-profile";
        }

        UserDTO currentUser = userService.findById(profileUpdateRequest.getUserId());

        if (!currentUser.getUserName().equals(profileUpdateRequest.getUserName())) {
            UserDTO existingUser = userService.findByUsername(profileUpdateRequest.getUserName());
            if (existingUser != null && existingUser.getUserId() != currentUser.getUserId()) {
                model.addAttribute("my_error", "Username already exists!");
                return "admin/my-profile";
            }
        }

        if (!currentUser.getEmail().equals(profileUpdateRequest.getEmail())) {
            UserDTO existingUser = userService.findByEmail(profileUpdateRequest.getEmail());
            if (existingUser != null && existingUser.getUserId() != currentUser.getUserId()) {
                model.addAttribute("my_error", "The email already exists!");
                return "admin/my-profile";
            }
        }

        BeanUtils.copyProperties(profileUpdateRequest, currentUser, "userId");
        userService.update(currentUser);
        redirectAttributes.addFlashAttribute("success_message", "Profile updated successfully!");
        return "redirect:/admin/my-profile";
    }

    @PostMapping("/admin/my-profile/change-password")
    public String changePassword(
            @Valid @ModelAttribute("changePasswordRequest") ChangePasswordRequest changePasswordRequest,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        UserDTO user = getCurrentUser();
        ProfileUpdateRequest profileUpdateRequest = createProfileUpdateRequest(user);
        model.addAttribute("profileUpdateRequest", profileUpdateRequest);


        if (bindingResult.hasErrors()) {
            return "admin/my-profile";
        }

        if (!userService.checkPassword(user, changePasswordRequest.getCurrentPassword())) {
            model.addAttribute("error_message", "Current password is incorrect!");
            return "admin/my-profile";
        }

        if (!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmPassword())) {
            model.addAttribute("error_message", "New passwords do not match!");
            return "admin/my-profile";
        }

        userService.updatePassword(user.getUserId(), changePasswordRequest.getNewPassword());
        redirectAttributes.addFlashAttribute("success_message", "Password updated successfully!");

        return "redirect:/admin/my-profile";
    }


}
