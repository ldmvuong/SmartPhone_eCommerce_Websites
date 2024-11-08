package vn.ute.smartphoneshop.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import vn.ute.smartphoneshop.entity.BrandEntity;
import vn.ute.smartphoneshop.entity.CartEntity;
import vn.ute.smartphoneshop.service.IBrandService;

import java.util.List;

@Controller("userHomeController")
public class HomeController {
    @Autowired
    IBrandService brandService;

    @GetMapping("/home")
    public String index(Model model) {
        List<BrandEntity> brandEntities = brandService.findAll();
        model.addAttribute("brands", brandEntities);
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
