package vn.ute.smartphoneshop.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller("adminHomeController")
public class HomeController {

    @GetMapping("/helloworld")
    public String helloWorld() {
        return "helloworld";
    }
    @GetMapping("/admin/home")
    public String home() {
        return "admin/index";
    }

}
