package vn.ute.smartphoneshop.controller.admin;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import vn.ute.smartphoneshop.entity.ProductEntity;
import vn.ute.smartphoneshop.model.dto.ProductDTO;
import vn.ute.smartphoneshop.service.ProductService;
import vn.ute.smartphoneshop.service.impl.ProductServiceImpl;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class ProductController {
    @Autowired
    private ProductServiceImpl productServiceImpl;

    @RequestMapping(value = "/product-list", method = RequestMethod.GET)
    public ModelAndView productList(HttpServletRequest request ) {
        ModelAndView mav = new ModelAndView("admin/product-list");
        List<ProductDTO> products = productServiceImpl.findAllProduct();
        mav.addObject("products", products);
        return mav;
    }
    @GetMapping("/add-product")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new ProductDTO());
        return "admin/add-product";
    }

    @PostMapping("/add-product")
    public String addProduct(@ModelAttribute("product") ProductDTO product) {
        productServiceImpl.save(product);
        return "redirect:/admin/product-list";
    }

//    @RequestMapping(value ="/admin/add-product", method = RequestMethod.GET )
//    public String showAddProduct(Model model) {
//        model.addAttribute("product", new ProductDTO());
//        return "admin/add-product";
//    }

//    @PostMapping("/admin/add-product")
//    public ModelAndView addProduct(@ModelAttribute("product") ProductDTO product) {
//        productServiceImpl.save(product);
//        return new ModelAndView("redirect:/admin/product-list");
//    }
}
