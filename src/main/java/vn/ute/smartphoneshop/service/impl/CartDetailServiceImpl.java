package vn.ute.smartphoneshop.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.ute.smartphoneshop.entity.CartDetailEntity;
import vn.ute.smartphoneshop.entity.CartEntity;
import vn.ute.smartphoneshop.entity.ProductEntity;
import vn.ute.smartphoneshop.model.dto.CartDetailDTO;
import vn.ute.smartphoneshop.model.request.CartDetailRequest;
import vn.ute.smartphoneshop.repository.CartRepository;
import vn.ute.smartphoneshop.repository.ICartDetailRepository;
import vn.ute.smartphoneshop.repository.ProductRepository;
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
    public boolean insert(CartDetailDTO cartDetailDTO) {
        try {
            CartDetailEntity cartDetailEntity = new CartDetailEntity();
            CartEntity cartEntity = cartRepository.findById(cartDetailDTO.getCartId()).orElse(null);
            ProductEntity productEntity = productRepository.findById(cartDetailDTO.getProductId()).orElse(null);

            cartDetailEntity.setCart(cartEntity);
            cartDetailEntity.setProduct(productEntity);
            BeanUtils.copyProperties(cartDetailDTO, cartDetailEntity);

            cartEntity.setTotalPrice(cartEntity.getTotalPrice()+cartDetailDTO.getCartPrice());

            cartDetailRepository.save(cartDetailEntity);
            cartRepository.save(cartEntity);
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

            cartDetailRepository.save(cartDetailEntity);
            cartRepository.save(cartEntity);
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


}
