package vn.ute.smartphoneshop.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/orders")
public class OderController {

    @GetMapping("/order-list")
    public String orderList(){
        return "admin/oder-list";
    }

    @GetMapping("/order-detail")
    public String orderList(Model model){
        return "admin/oder-detail";
    }
}
