package vn.ute.smartphoneshop.service;

import vn.ute.smartphoneshop.model.dto.WishlistDTO;

import java.util.List;

public interface IWishlistService {
    List<WishlistDTO> getWishlistsByUserId(int userId);

    WishlistDTO findByUserIdAndProductId(int userId, int productId);

    boolean addWishlist(WishlistDTO wishlistDTO);
    boolean deleteWishlist(WishlistDTO wishlistDTO);
}
