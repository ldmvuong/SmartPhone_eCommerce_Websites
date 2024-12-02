package vn.ute.smartphoneshop.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.ute.smartphoneshop.enums.OrderStatus;
import vn.ute.smartphoneshop.model.response.OrderDetaiRespone;
import vn.ute.smartphoneshop.model.response.OrderRespone;
import vn.ute.smartphoneshop.service.IOrderService;

import java.util.Optional;

@Controller
@RequestMapping("/admin/orders")
public class OrderController {

    @Autowired
    private IOrderService orderService;


    @GetMapping("/order-list")
    public String orderList(@RequestParam(value = "status", required = false) String status,
                            @RequestParam(value = "page", defaultValue = "1") int page,
                            Model model) {
        int pageSize = 10;
        Pageable pageable = PageRequest.of(page - 1, pageSize);

        Page<OrderRespone> orderPage;

        if (status == null || status.isEmpty()) {
            orderPage = orderService.getAllOrders(pageable);
        } else {
            try {
                // Chuyển đổi chuỗi thành enum OrderStatus
                OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
                orderPage = orderService.getOrdersByStatus(orderStatus, pageable);
            } catch (IllegalArgumentException e) {
                orderPage = orderService.getAllOrders(pageable);
                model.addAttribute("error", "Invalid order status");
            }
        }

        model.addAttribute("orders", orderPage.getContent());
        model.addAttribute("totalPages", orderPage.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("status", status);

        return "admin/oder-list";
    }

    @GetMapping("/order-detail/{orderID}")
    public String getOrderDetail(@PathVariable("orderID") int orderID, Model model) {
        Optional<OrderDetaiRespone> orderDetail = orderService.getOrderDetailById(orderID);

        if (orderDetail.isPresent()) {
            model.addAttribute("orderDetail", orderDetail.get());
            return "admin/oder-detail";
        } else {
            model.addAttribute("error", "Order not found");
            return "error";
        }
    }
}
