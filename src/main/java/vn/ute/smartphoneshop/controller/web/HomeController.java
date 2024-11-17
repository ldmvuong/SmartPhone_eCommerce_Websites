//package vn.ute.smartphoneshop.controller.web;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import vn.ute.smartphoneshop.entity.BrandEntity;
//import vn.ute.smartphoneshop.entity.CartEntity;
//import vn.ute.smartphoneshop.entity.ProductEntity;
//import vn.ute.smartphoneshop.model.request.CartDetailRequest;
//import vn.ute.smartphoneshop.service.IBrandService;
//import vn.ute.smartphoneshop.service.IProductService;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Controller("webHomeController")
//public class HomeController {
//    @Autowired
//    IBrandService brandService;
//
//    @Autowired
//    IProductService productService;
//
//    @GetMapping("/home")
//    public String index(Model model) {
//        List<BrandEntity> brandEntities = brandService.findAll();
//
//        List<ProductEntity> newArrivalProductList = productService.newArrivalProduct();
//        if (newArrivalProductList.size() > 12){
//            newArrivalProductList = newArrivalProductList.subList(0, 12);
//        }
//
//        CartEntity cartEntity = cartService.findCartByUserId(getCurrentUser().getUserId());
//        List<CartDetailRequest> cartDetailRequestList = new ArrayList<>();
//        if(cartEntity != null){
//            cartDetailRequestList = cartDetailService.findByCartId(cartEntity.getCartId());
//        }
//
//        model.addAttribute("cart", cartEntity);
//
//        model.addAttribute("newArrivalProducts", newArrivalProductList);
//        model.addAttribute("brands", brandEntities);
//
//        return "web/index";
//    }
//
//    @GetMapping("/product-list")
//    public String productList() {
//        return "web/shop-right-sidebar";
//    }
//
//    @GetMapping("/about-us")
//    public String aboutUs() {
//        return "web/about";
//    }
//
//}
