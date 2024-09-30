package vn.ute.smartphoneshop.service;

import vn.ute.smartphoneshop.dtos.OrderDTO;
import vn.ute.smartphoneshop.entity.OrderEntity;

import java.util.List;

public interface IOrderService {
    OrderEntity createOrder(OrderDTO order);
    OrderEntity updateOrder(OrderDTO order);
    OrderEntity deleteOrder(int orderId);
    OrderEntity getOrder(int orderId);
    List<OrderEntity> getAllOrders();
}
