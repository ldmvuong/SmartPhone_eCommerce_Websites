package vn.ute.smartphoneshop.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.ute.smartphoneshop.model.dto.UserDTO;
import vn.ute.smartphoneshop.service.IRoleService;
import vn.ute.smartphoneshop.service.IUserService;
import vn.ute.smartphoneshop.service.impl.UserServiceImpl;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class UserController {
    @Autowired
    private IUserService userService;

    @Autowired
    private IRoleService roleService;

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

    @PostMapping(value = "/add-user")
    public String addNewUser(@ModelAttribute("user") UserDTO user) {
        if(user.getConfirmPassword().equals(user.getPassword())&&roleService.findById(user.getRoleId())&&userService.add(user)) {
            return "redirect:/admin/user-list";
        }
        return "admin/add-new-user";
    }
}
