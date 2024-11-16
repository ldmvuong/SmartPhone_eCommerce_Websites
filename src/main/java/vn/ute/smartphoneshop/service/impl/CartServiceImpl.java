package vn.ute.smartphoneshop.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.ute.smartphoneshop.entity.CartEntity;
import vn.ute.smartphoneshop.entity.UserEntity;
import vn.ute.smartphoneshop.model.dto.CartDTO;
import vn.ute.smartphoneshop.repository.CartRepository;
import vn.ute.smartphoneshop.repository.UserRepository;
import vn.ute.smartphoneshop.service.ICartService;

@Service
public class CartServiceImpl implements ICartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean createCart(CartDTO cart) {
        try {
            UserEntity userEntity = userRepository.findById(cart.getUserId()).get();
            if(userEntity != null){
                CartEntity cartEntity = new CartEntity();
                cartEntity.setUser(userEntity);
                cartEntity.setTotalPrice(cart.getTotalPrice());
                cartRepository.save(cartEntity);
                return true;
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateCart(CartEntity cart) {
        try {
            if(userRepository.findByUserId(cart.getUser().getUserId()) != null){
                cartRepository.save(cart);
                return true;
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteCart(CartDTO cart) {
        try {
            if(userRepository.findByUserId(cart.getUserId()) != null){
                CartEntity cartEntity = cartRepository.findByUser_UserId(cart.getUserId()).orElse(null);
                BeanUtils.copyProperties(cart, cartEntity);
                cartRepository.delete(cartEntity);
                return true;
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public CartDTO getCartByUserId(int userId) {
        CartEntity cartEntity = cartRepository.findByUser_UserId(userId).orElse(null);
        CartDTO cartDTO = new CartDTO();
        BeanUtils.copyProperties(cartEntity, cartDTO);
        return cartDTO;
    }

    @Override
    public CartEntity findCartByUserId(int userId) {
        return cartRepository.findByUser_UserId(userId).orElse(null);
    }

    @Override
    public CartEntity findCartById(int cartId) {
        return cartRepository.findById(cartId).orElse(null);
    }
}
