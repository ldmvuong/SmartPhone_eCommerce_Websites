package vn.ute.smartphoneshop.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.ute.smartphoneshop.entity.CartDetailEntity;
import vn.ute.smartphoneshop.entity.CartEntity;
import vn.ute.smartphoneshop.entity.ProductEntity;
import vn.ute.smartphoneshop.entity.WishlistEntity;
import vn.ute.smartphoneshop.model.dto.CartDetailDTO;
import vn.ute.smartphoneshop.model.request.CartDetailRequest;
import vn.ute.smartphoneshop.repository.CartRepository;
import vn.ute.smartphoneshop.repository.ICartDetailRepository;
import vn.ute.smartphoneshop.repository.ProductRepository;
import vn.ute.smartphoneshop.repository.WishlistRepository;
import vn.ute.smartphoneshop.service.ICartDetailService;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartDetailServiceImpl implements ICartDetailService {

    @Autowired
    private ICartDetailRepository cartDetailRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private WishlistRepository wishlistRepository;

    @Override
    public List<CartDetailRequest> findByCartId(int cartId) {
        List<CartDetailEntity> list = cartDetailRepository.findByCart_CartId(cartId);

        List<CartDetailRequest> cartDetailRequestList = new ArrayList<CartDetailRequest>();
        for (CartDetailEntity cartDetailEntity : list) {
            CartDetailRequest cartDetailRequest = new CartDetailRequest();
            cartDetailRequest.setCartPrice(cartDetailEntity.getCartPrice());
            cartDetailRequest.setCartId(cartId);
            cartDetailRequest.setProductId(cartDetailEntity.getProduct());
            cartDetailRequest.setQuantity(cartDetailEntity.getQuantity());
            cartDetailRequestList.add(cartDetailRequest);
        }
        return cartDetailRequestList;
    }

    @Override
    public CartDetailRequest convertCartDetailRequest(CartDetailEntity cartDetailEntity){
        CartDetailRequest cartDetailRequest = new CartDetailRequest();
        BeanUtils.copyProperties(cartDetailEntity, cartDetailRequest);
        cartDetailRequest.setCartPrice(cartDetailEntity.getCartPrice());
        cartDetailRequest.setProductId(cartDetailEntity.getProduct());
        cartDetailRequest.setQuantity(cartDetailEntity.getQuantity());
        return cartDetailRequest;
    }

    @Override
    public boolean insert(CartDetailDTO cartDetailDTO) {
        try {
            CartDetailEntity cartDetailEntity = new CartDetailEntity();
            CartEntity cartEntity = cartRepository.findById(cartDetailDTO.getCartId()).orElse(null);
            ProductEntity productEntity = productRepository.findById(cartDetailDTO.getProductId()).orElse(null);

            cartDetailEntity.setCart(cartEntity);
            cartDetailEntity.setProduct(productEntity);
            BeanUtils.copyProperties(cartDetailDTO, cartDetailEntity);

            cartEntity.setTotalPrice(cartEntity.getTotalPrice()+cartDetailDTO.getCartPrice());

            WishlistEntity wishlistEntity = wishlistRepository.findByProduct_ProductIdAndAndUser_UserId(productEntity.getProductId(),cartEntity.getUser().getUserId()).orElse(null);

            cartDetailRepository.save(cartDetailEntity);
            cartRepository.save(cartEntity);

            if (wishlistEntity != null) {
                wishlistRepository.delete(wishlistEntity);
            }
            return true;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(CartDetailDTO cartDetailDTO){
        try {
            CartEntity cartEntity = cartRepository.findById(cartDetailDTO.getCartId()).orElse(null);
            CartDetailEntity cartDetailEntity = cartDetailRepository.findByCart_CartIdAndAndProduct_ProductId(cartDetailDTO.getCartId(), cartDetailDTO.getProductId()).orElse(null);

            cartEntity.setTotalPrice(cartEntity.getTotalPrice()-cartDetailEntity.getCartPrice()+cartDetailDTO.getCartPrice());

            cartDetailEntity.setQuantity(cartDetailDTO.getQuantity());
            cartDetailEntity.setCartPrice(cartDetailDTO.getCartPrice());

            WishlistEntity wishlistEntity = wishlistRepository.findByProduct_ProductIdAndAndUser_UserId(cartDetailEntity.getProduct().getProductId(),cartEntity.getUser().getUserId()).orElse(null);

            cartDetailRepository.save(cartDetailEntity);
            cartRepository.save(cartEntity);

            if (wishlistEntity != null) {
                wishlistRepository.delete(wishlistEntity);
            }

            return true;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(int cartDetailId) {
        try {
            CartDetailEntity cartDetailEntity = cartDetailRepository.findById(cartDetailId).orElse(null);
            CartEntity cartEntity = cartDetailEntity.getCart();

            if (cartDetailEntity != null) {
                cartEntity.setTotalPrice(cartEntity.getTotalPrice()-cartDetailEntity.getCartPrice());
                cartDetailRepository.delete(cartDetailEntity);
                cartRepository.save(cartEntity);
                return true;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public CartDetailEntity findByCartIdAndProductId(int cartId, int productId) {
        return cartDetailRepository.findByCart_CartIdAndAndProduct_ProductId(cartId,productId).orElse(null);
    }

    @Transactional
    @Override
    public void deleteAllByCartId(int cartId) {
        cartDetailRepository.deleteByCart_CartId(cartId);
        CartEntity cart = cartRepository.findById(cartId).orElseThrow(() -> new RuntimeException("Cart not found"));
        cart.setTotalPrice(0L);
        cartRepository.save(cart);

    }


}
