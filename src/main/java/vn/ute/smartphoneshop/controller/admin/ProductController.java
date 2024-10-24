package vn.ute.smartphoneshop.controller.admin;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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
@Transactional
public class ProductController {

    @Autowired
    private ProductServiceImpl productServiceImpl;

    @Autowired
    private ProductService productService;

    @RequestMapping(value = "/product-list", method = RequestMethod.GET)
    public ModelAndView productList(HttpServletRequest request ) {
        ModelAndView mav = new ModelAndView("admin/product-list");
        List<ProductDTO> products = productService.findAllProduct();
        mav.addObject("products", products);
        return mav;
    }
    @GetMapping("/add-product")
    public String showAddNewProductForm(Model model) {
        model.addAttribute("product", new ProductDTO());
        return "admin/add-product";
    }

    @GetMapping("/add-product/{id}")
    public String showAddProductForm(@PathVariable(value = "id", required = false) Integer id, Model model) {
        if (id != null) {
            ProductDTO product = productService.findProductById(id);
            model.addAttribute("product", product);
        } else {
            model.addAttribute("product", new ProductDTO());
        }
        return "admin/add-product";
    }

    @PostMapping("/add-product")
    public String addProduct(@ModelAttribute("product") ProductDTO product) {
        productService.save(product);
        return "redirect:/admin/product-list";
    }

    @GetMapping("/delete-product/{id}")
    public String deleteProduct(@PathVariable("id") Integer id) {
        productService.deleteProductById(id);
            return "redirect:/admin/product-list";
    }
    @GetMapping("/order-list")
    public String orderList(){
        return "admin/oder-list";
    }

    @GetMapping("/order-detail")
    public String orderList(Model model){
        return "admin/oder-detail";
    }

    @GetMapping(value ="/brand-list")
    public String brandList(){
        return "admin/category-list";
    }

    @GetMapping(value = "/add-brand")
    public String addBrand(Model model){
        return "admin/new-category";
    }
}
