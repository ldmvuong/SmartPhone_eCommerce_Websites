package vn.ute.smartphoneshop.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import vn.ute.smartphoneshop.model.dto.OrderDTO;
import vn.ute.smartphoneshop.entity.OrderEntity;
import vn.ute.smartphoneshop.entity.UserEntity;
import vn.ute.smartphoneshop.entity.VoucherEntity;
import vn.ute.smartphoneshop.exception.DataNotFoundException;
import vn.ute.smartphoneshop.repository.OrderRepository;
import vn.ute.smartphoneshop.repository.UserRepository;
import vn.ute.smartphoneshop.repository.VoucherRepository;
import vn.ute.smartphoneshop.service.IOrderService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements IOrderService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final VoucherRepository voucherRepository;
    private final ModelMapper modelMapper;
    @Override
    public OrderEntity createOrder(OrderDTO order) {
//        Kiem tra user co ton tai trong ds khach hang khong
        UserEntity user = userRepository.findById(order.getUser_id());
//        Kiem tra voucher co ton tai trong ds voucher khong
        VoucherEntity voucher = null;
        if(order.getVoucher_id() > 0){
            voucher = voucherRepository.findById(order.getVoucher_id()).orElseThrow(()->new DataNotFoundException("Voucher not found"));
        }
//        Anh xa OrderDTO sang OrderEntity
        OrderEntity orderEntity = new OrderEntity();

        modelMapper.typeMap(OrderDTO.class, OrderEntity.class).addMappings(mapper -> mapper.skip(OrderEntity::setOrderId));
        modelMapper.map(order, orderEntity);
        orderEntity.setUser(user);
        if(voucher != null){
            orderEntity.setVoucher(voucher);
        }
        orderRepository.save(orderEntity);
        return orderEntity;
    }

    @Override
    public OrderEntity updateOrder(OrderDTO order) {
        return null;
    }

    @Override
    public OrderEntity deleteOrder(int orderId) {
        return null;
    }

    @Override
    public OrderEntity getOrder(int orderId) {
        return null;
    }

    @Override
    public List<OrderEntity> getAllOrders() {
        return List.of();
    }
}
