package vn.ute.smartphoneshop.controller.admin;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import vn.ute.smartphoneshop.model.dto.UserDTO;
import vn.ute.smartphoneshop.service.IRoleService;
import vn.ute.smartphoneshop.service.IUserService;
import vn.ute.smartphoneshop.service.impl.UserServiceImpl;

import java.util.List;

@Controller("userAdminController")
@RequestMapping("/admin")
public class UserController {
    @Autowired
    private IUserService userService;

    @Autowired
    private IRoleService roleService;

//    @GetMapping("/user-list")
//    public String userList(Model model) {
//        List<UserDTO> users = userService.findAll();
//        model.addAttribute("users", users);
//        return "admin/all-user";
//    }

    @GetMapping("/user-list")
    public String userList(@RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "10") int size,
                           Model model) {

        Pageable pageable = PageRequest.of(page, size);

        Page<UserDTO> userPage = userService.findAll(pageable);

        model.addAttribute("users", userPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("totalItems", userPage.getTotalElements());

        return "admin/all-user";
    }


    @GetMapping("/add-new-user")
    public String addNewUser(Model model) {
        model.addAttribute("user", new UserDTO());
        return "admin/add-new-user";
    }

    @PostMapping(value = "/add-user")
    public String addNewUser(@Valid @ModelAttribute("user") UserDTO user, BindingResult result, Model model) {
        if(result.hasErrors()) {
            return "admin/add-new-user";
        }
        if(user.getUserId() != 0){
            if(roleService.findById(user.getRoleId())&& userService.update(user)){
                return "redirect:/admin/user-list";
            }
        }
        else if(user.getConfirmPassword().equals(user.getPassword())&&roleService.findById(user.getRoleId())&&userService.add(user)) {
            return "redirect:/admin/user-list";
        }
        return "admin/add-new-user";
    }
    @PostMapping(value = "/add-user/{id}")
    public String addNewUser(@ModelAttribute("user") UserDTO user,@PathVariable(value = "id") Integer id) {
        if(roleService.findById(user.getRoleId())&&userService.update(user)) {
            return "redirect:/admin/user-list";
        }
        return "redirect:admin/add-new-user/"+user.getUserId();
    }

    @GetMapping("/add-user/{id}")
    public String addNewUser(@PathVariable(value = "id", required = false) Integer id, Model model) {
        if (id != null) {
            UserDTO user = userService.findById(id);
            model.addAttribute("user", user);
        }
        else {
            model.addAttribute("user", new UserDTO());
        }
        return "admin/add-new-user";
    }
}
