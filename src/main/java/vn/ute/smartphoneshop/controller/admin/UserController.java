package vn.ute.smartphoneshop.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.ute.smartphoneshop.model.dto.UserDTO;
import vn.ute.smartphoneshop.service.IUserService;
import vn.ute.smartphoneshop.service.impl.UserServiceImpl;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class UserController {
    @Autowired
    private IUserService userService;

    @GetMapping("/user-list")
    public String userList(Model model) {
        List<UserDTO> users = userService.findAll();
        model.addAttribute("users", users);
        return "admin/all-user";
    }

    @GetMapping("/add-new-user")
    public String addNewUser(Model model) {
        model.addAttribute("user", new UserDTO());
        return "admin/add-new-user";
    }
}
