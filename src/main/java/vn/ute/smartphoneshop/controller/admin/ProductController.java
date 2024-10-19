package vn.ute.smartphoneshop.controller.admin;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController(value = "productControllerOfAdmin")
public class ProductController {

    @RequestMapping(value = "/admin/product-list", method = RequestMethod.GET)
    public ModelAndView productList(HttpServletRequest request ) {
        ModelAndView mav = new ModelAndView("admin/product-list");
        return mav;
    }
}
