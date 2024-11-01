package vn.ute.smartphoneshop.controller.admin;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import vn.ute.smartphoneshop.model.dto.ProductDTO;
import vn.ute.smartphoneshop.service.ProductService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
@Transactional
public class ProductController {

    @Autowired
    private ProductService productService;
    @Value("${upload.path}")
    private String uploadPath;

    @RequestMapping(value = "/product-list", method = RequestMethod.GET)
    public ModelAndView productList() {
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
    public String showAddProductForm(@PathVariable("id") Integer id, Model model) {
        ProductDTO product = productService.findProductById(id);
        model.addAttribute("product", product != null ? product : new ProductDTO());
        return "admin/add-product";
    }

    @PostMapping("/add-product")
    public String addProduct(@Valid @ModelAttribute("product") ProductDTO product,
                             BindingResult result,
                             @RequestParam("file") MultipartFile file,
                             Model model) {

        if (result.hasErrors()) {
            return "admin/add-product";
        }

        // Kiểm tra xem file có được chọn hay không
        if (!file.isEmpty()) {
            // Xử lý upload file ảnh
            try {
                // Tạo tên file duy nhất bằng UUID
                String originalFileName = file.getOriginalFilename();
                String fileName = UUID.randomUUID() + "_" + (originalFileName != null ? originalFileName : "image.jpg");

                // Đảm bảo đường dẫn uploadPath đã được cấu hình và hợp lệ
                Path path = Paths.get(uploadPath, fileName);

                // Tạo thư mục nếu chưa tồn tại
                Files.createDirectories(path.getParent());

                // Chuyển file đến thư mục đích
                file.transferTo(path.toFile());

                // Thiết lập đường dẫn ảnh vào product
                product.setImagePath("/uploads/products/" + fileName);

            } catch (IOException e) {
                e.printStackTrace();
                model.addAttribute("fileError", "Error uploading image: " + e.getMessage());
                return "admin/add-product";
            }
        } else {
            model.addAttribute("fileError", "Please select an image to upload");
            return "admin/add-product";
        }

        // Lưu thông tin product vào database
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
