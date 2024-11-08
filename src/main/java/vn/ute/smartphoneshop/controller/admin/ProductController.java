package vn.ute.smartphoneshop.controller.admin;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import vn.ute.smartphoneshop.entity.BrandEntity;
import vn.ute.smartphoneshop.model.dto.ProductDTO;
import vn.ute.smartphoneshop.service.IBrandService;
import vn.ute.smartphoneshop.service.IProductService;

import java.util.List;

@Controller
@RequestMapping("/admin/products")
@Transactional
public class ProductController {

    @Autowired
    private IProductService productService;

    @Autowired
    private IBrandService brandService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ModelAndView productList() {
        ModelAndView mav = new ModelAndView("admin/product-list");
        List<ProductDTO> products = productService.findAllProduct();
        mav.addObject("products", products);
        return mav;
    }

    @GetMapping("/save")
    public String showAddNewProductForm(Model model) {
        model.addAttribute("product", new ProductDTO());
        List<BrandEntity> brands = brandService.findAll();
        model.addAttribute("brands", brands);
        return "admin/add-product";
    }

    @GetMapping("/save/{id}")
    public String showAddProductForm(@PathVariable("id") Integer id, Model model) {
        ProductDTO product = productService.findProductById(id);
        model.addAttribute("product", product != null ? product : new ProductDTO());
        List<BrandEntity> brands = brandService.findAll();
        model.addAttribute("brands", brands);
        return "admin/add-product";
    }

    @PostMapping("/save")
    public String addProduct(@Valid @ModelAttribute("product") ProductDTO product,
                             BindingResult result,
                             @RequestParam("file") MultipartFile file,
                             @RequestParam("existingImagePath") String existingImagePath, // Đọc từ input ẩn
                             Model model) {

        if (result.hasErrors()) {
            List<BrandEntity> brands = brandService.findAll();
            model.addAttribute("brands", brands);
            return "admin/add-product";
        }

        // Giữ lại imagePath cũ nếu không có ảnh mới được tải lên
        if ((file == null || file.isEmpty()) && existingImagePath != null) {
            product.setImagePath(existingImagePath);
        }

        try {
            productService.saveProduct(product, file, existingImagePath); // Truyền `existingImagePath` vào service
        } catch (IllegalArgumentException e) {
            model.addAttribute("msg", e.getMessage());
            return "admin/add-product";
        } catch (RuntimeException e) {
            model.addAttribute("msg", "An error occurred while saving the product: " + e.getMessage());
            return "admin/add-product";
        }

        return "redirect:/admin/products";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Integer id) {
        productService.deleteProductById(id);
        return "redirect:/admin/products";
    }
}
