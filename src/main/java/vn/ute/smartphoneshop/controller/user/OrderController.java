package vn.ute.smartphoneshop.controller.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import vn.ute.smartphoneshop.model.dto.OrderDTO;
import vn.ute.smartphoneshop.entity.OrderEntity;
import vn.ute.smartphoneshop.model.dto.ProductDTO;
import vn.ute.smartphoneshop.model.dto.UserDTO;
import vn.ute.smartphoneshop.service.IOrderService;
import vn.ute.smartphoneshop.service.IUserService;

import java.util.List;

@Controller("orderUserController")
@RequestMapping("/user/orders")
public class OrderController{
    @Autowired
    IOrderService orderService;

    @Autowired
    private IUserService userService;

    private UserDTO getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = (principal instanceof UserDetails) ? ((UserDetails) principal).getUsername() : principal.toString();
        return userService.findByUsername(username);
    }

    @GetMapping("")
    public String orders(Model model) {
        UserDTO currentUser = getCurrentUser();
        OrderDTO orderDTO = orderService.getOrderDTOByUserId(currentUser.getUserId());
//        List<ProductDTO> productDTOS = orderService.getOrder()
        return "user/orders";
    }
//    private final IOrderService orderService ;
//    @PostMapping("")
//    public ResponseEntity<?> createOrder(@RequestBody @Valid OrderDTO orderDTO,
//                                         BindingResult bindingResult){
//        try {
//            if(bindingResult.hasErrors()){
//                List<String> errors = bindingResult.getFieldErrors()
//                        .stream().map(FieldError::getDefaultMessage)
//                        .toList();
//                return ResponseEntity.badRequest().body(errors);
//            }
//            OrderEntity orderEntity = orderService.createOrder(orderDTO);
//            return ResponseEntity.ok(orderEntity);
//        }catch (Exception e){
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
}
