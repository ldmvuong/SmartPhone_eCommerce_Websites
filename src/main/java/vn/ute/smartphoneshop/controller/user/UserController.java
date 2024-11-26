package vn.ute.smartphoneshop.controller.user;

import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.ute.smartphoneshop.entity.BrandEntity;
import vn.ute.smartphoneshop.entity.CartEntity;
import vn.ute.smartphoneshop.entity.ProductEntity;
import vn.ute.smartphoneshop.model.dto.UserDTO;
import vn.ute.smartphoneshop.model.request.CartDetailRequest;
import vn.ute.smartphoneshop.model.request.ChangePasswordRequest;
import vn.ute.smartphoneshop.model.request.ProfileUpdateRequest;
import vn.ute.smartphoneshop.service.IBrandService;
import vn.ute.smartphoneshop.service.ICartDetailService;
import vn.ute.smartphoneshop.service.ICartService;
import vn.ute.smartphoneshop.service.IUserService;
import vn.ute.smartphoneshop.utils.SecurityUtil;

import java.util.ArrayList;
import java.util.List;

@Controller("userController")
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    IBrandService brandService;

    @Autowired
    ICartDetailService cartDetailService;

    @Autowired
    ICartService cartService;


    private UserDTO getCurrentUser() {
        String username = SecurityUtil.getCurrentUsername();
        return userService.findByUsername(username);
    }

    private ProfileUpdateRequest createProfileUpdateRequest(UserDTO user) {
        ProfileUpdateRequest profileUpdateRequest = new ProfileUpdateRequest();
        BeanUtils.copyProperties(user, profileUpdateRequest);
        return profileUpdateRequest;
    }


    @GetMapping("/my-profile")
    public String myProfile(Model model) {
        UserDTO user = getCurrentUser();
        ProfileUpdateRequest profileUpdateRequest = createProfileUpdateRequest(user);
        model.addAttribute("profileUpdateRequest", profileUpdateRequest);
        model.addAttribute("changePasswordRequest", new ChangePasswordRequest());

        List<BrandEntity> brandEntities = brandService.findAll();



        CartEntity cartEntity = new CartEntity();
        List<CartDetailRequest> cartDetailRequestList = new ArrayList<>();

        if (getCurrentUser() != null) {
            cartEntity = cartService.findCartByUserId(getCurrentUser().getUserId());
            if(cartEntity != null){
                cartDetailRequestList = cartDetailService.findByCartId(cartEntity.getCartId());
            }
        }

        model.addAttribute("cart", cartEntity);
        model.addAttribute("cartDetailList", cartDetailRequestList);
        model.addAttribute("brands", brandEntities);
        return "web/my-account";
    }

    @PostMapping("/my-profile/update")
    public String updateProfile(
            @Valid @ModelAttribute("profileUpdateRequest") ProfileUpdateRequest profileUpdateRequest,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        model.addAttribute("changePasswordRequest", new ChangePasswordRequest());

        if (bindingResult.hasErrors()) {
            return "web/my-account";
        }

        UserDTO currentUser = userService.findById(profileUpdateRequest.getUserId());

        if (!currentUser.getUserName().equals(profileUpdateRequest.getUserName())) {
            UserDTO existingUser = userService.findByUsername(profileUpdateRequest.getUserName());
            if (existingUser != null && existingUser.getUserId() != currentUser.getUserId()) {
                model.addAttribute("my_error", "Username already exists!");
                return "web/my-account";
            }
        }

        if (!currentUser.getEmail().equals(profileUpdateRequest.getEmail())) {
            UserDTO existingUser = userService.findByEmail(profileUpdateRequest.getEmail());
            if (existingUser != null && existingUser.getUserId() != currentUser.getUserId()) {
                model.addAttribute("my_error", "The email already exists!");
                return "web/my-account";
            }
        }

        BeanUtils.copyProperties(profileUpdateRequest, currentUser, "userId");
        userService.update(currentUser);
        redirectAttributes.addFlashAttribute("success_message", "Profile updated successfully!");
        return "redirect:/user/my-profile";
    }

    @PostMapping("/my-profile/change-password")
    public String changePassword(
            @Valid @ModelAttribute("changePasswordRequest") ChangePasswordRequest changePasswordRequest,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        UserDTO user = getCurrentUser();
        ProfileUpdateRequest profileUpdateRequest = createProfileUpdateRequest(user);
        model.addAttribute("profileUpdateRequest", profileUpdateRequest);

        if (bindingResult.hasErrors()) {
            return "web/my-account";
        }

        if (!userService.checkPassword(user, changePasswordRequest.getCurrentPassword())) {
            model.addAttribute("error_message", "Current password is incorrect!");
            return "web/my-account";
        }

        if (!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmPassword())) {
            model.addAttribute("error_message", "New passwords do not match!");
            return "web/my-account";
        }

        userService.updatePassword(user.getUserId(), changePasswordRequest.getNewPassword());
        redirectAttributes.addFlashAttribute("success_message", "Password updated successfully!");
        return "redirect:/user/my-profile";
    }
}
