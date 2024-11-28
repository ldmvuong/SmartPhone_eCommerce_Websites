package vn.ute.smartphoneshop.service;

import vn.ute.smartphoneshop.entity.CartDetailEntity;
import vn.ute.smartphoneshop.model.dto.CartDetailDTO;
import vn.ute.smartphoneshop.model.request.CartDetailRequest;

import java.util.List;
import java.util.Optional;

public interface ICartDetailService {
    List<CartDetailRequest> findByCartId(int cartId);
    boolean insert(CartDetailDTO cartDetailDTO);

    boolean update(CartDetailDTO cartDetailDTO);

    //    boolean update(CartDetailRequest cartDetailRequest);
    boolean delete(int cartId);
    CartDetailEntity findByCartIdAndProductId(int cartId,int productId);

    void deleteAllByCartId(int cartId);
}
