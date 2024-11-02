package vn.ute.smartphoneshop.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller("userHomeController")
public class HomeController {
    @GetMapping("/home")
    public String index() {
        return "web/index";
    }

    @GetMapping("/product-list")
    public String productList() {
        return "web/shop-right-sidebar";
    }

    @GetMapping("/about-us")
    public String aboutUs() {
        return "web/about";
    }

}
