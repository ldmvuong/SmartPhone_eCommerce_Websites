package vn.ute.smartphoneshop.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.ute.smartphoneshop.entity.BrandEntity;
import vn.ute.smartphoneshop.entity.ProductEntity;
import vn.ute.smartphoneshop.service.IBrandService;
import vn.ute.smartphoneshop.service.IProductService;

import java.util.ArrayList;
import java.util.List;

@Controller("userProductController")
@RequestMapping("/products")
public class ProductController {
    @Autowired
    IProductService productService;

    @Autowired
    IBrandService brandService;

    @GetMapping("")
    public String index(Model model, @RequestParam("brand") String brand) {
        List<BrandEntity> brandEntityList = brandService.findAll();
        List<ProductEntity> list = productService.findProductByBrandName(brand);

        model.addAttribute("products", list);
        model.addAttribute("brands", brandEntityList);
        return "web/shop-right-sidebar";
    }

}
