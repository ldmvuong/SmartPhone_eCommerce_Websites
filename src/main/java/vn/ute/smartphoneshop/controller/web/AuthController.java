package vn.ute.smartphoneshop.controller.web;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import vn.ute.smartphoneshop.model.dto.UserDTO;
import vn.ute.smartphoneshop.service.IUserService;

@Controller
public class AuthController {

    @Autowired
    private IUserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/showPage403")
    public String showPage403() {
        return "web/404";
    }

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("registerUser", new UserDTO());
        return "web/login";
    }

    @PostMapping("/process")
    public String processRegister(@Valid @ModelAttribute("registerUser") UserDTO userDTO,
                                  BindingResult bindingResult,
                                  Model model) {
        if (bindingResult.hasErrors()) {
            return "web/login";
        }

        if (!userDTO.getPassword().equals(userDTO.getConfirmPassword())) {
            model.addAttribute("my_error", "Passwords do not match!");
            return "web/login";
        }


        if (userService.findByUsername(userDTO.getUserName()) != null) {
            model.addAttribute("my_error", "The username already exists!");
            return "web/login";
        }

        if (userService.findByEmail(userDTO.getEmail()) != null) {
            model.addAttribute("my_error", "The email already exists!");
            return "web/login";
        }

        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userDTO.setRoleId(2);
        userService.add(userDTO);

        return "redirect:/login?registerSuccess=true";
    }
}
