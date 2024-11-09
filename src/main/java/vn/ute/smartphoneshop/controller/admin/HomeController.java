package vn.ute.smartphoneshop.controller.admin;

import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.ute.smartphoneshop.model.dto.UserDTO;
import vn.ute.smartphoneshop.model.request.ProfileUpdateRequest;
import vn.ute.smartphoneshop.service.IUserService;


@Controller("adminHomeController")
public class HomeController {

    @Autowired
    private IUserService userService;

    @GetMapping("/admin/home")
    public String home() {
        return "admin/index";
    }

    @GetMapping("/admin/my-profile")
    public String myProfile(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        UserDTO user = userService.findByUsername(username);
        model.addAttribute("profileUpdateRequest", user);
        return "admin/my-profile";
    }


    @PostMapping("/admin/my-profile/update")
    public String updateProfile(
            @Valid @ModelAttribute("profileUpdateRequest") ProfileUpdateRequest profileUpdateRequest,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

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


}
