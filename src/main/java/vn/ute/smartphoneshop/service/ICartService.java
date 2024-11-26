package vn.ute.smartphoneshop.service;

import vn.ute.smartphoneshop.entity.CartEntity;
import vn.ute.smartphoneshop.model.dto.CartDTO;

public interface ICartService {
    boolean createCart(CartDTO cart);
    boolean updateCart(CartDTO cart);
    boolean deleteCart(int cartId);
    CartDTO getCartByUserId(int userId);
    CartEntity findCartByUserId(int userId);

    CartEntity findCartById(int cartId);
}
