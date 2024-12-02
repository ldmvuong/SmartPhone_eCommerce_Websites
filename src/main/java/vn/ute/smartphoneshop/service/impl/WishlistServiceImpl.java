package vn.ute.smartphoneshop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.ute.smartphoneshop.converter.ProductDTOConverter;
import vn.ute.smartphoneshop.entity.ProductEntity;
import vn.ute.smartphoneshop.entity.UserEntity;
import vn.ute.smartphoneshop.entity.WishlistEntity;
import vn.ute.smartphoneshop.model.dto.ProductDTO;
import vn.ute.smartphoneshop.model.dto.WishlistDTO;
import vn.ute.smartphoneshop.repository.ProductRepository;
import vn.ute.smartphoneshop.repository.UserRepository;
import vn.ute.smartphoneshop.repository.WishlistRepository;
import vn.ute.smartphoneshop.service.IWishlistService;

import java.util.ArrayList;
import java.util.List;

@Service
public class WishlistServiceImpl implements IWishlistService {

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductDTOConverter productDTOConverter;

    @Override
    public List<WishlistDTO> getWishlistsByUserId(int userId) {
        List<WishlistDTO> list = new ArrayList<WishlistDTO>();
        List<WishlistEntity> wishlistEntities = wishlistRepository.findByUser_UserId(userId);
        for (WishlistEntity wishlistEntity : wishlistEntities) {
            WishlistDTO wishlistDTO = new WishlistDTO();
            wishlistDTO.setUserId(wishlistEntity.getUser().getUserId());
            ProductDTO productDTO = productDTOConverter.toProductDTO(wishlistEntity.getProduct());
                productDTO.setBrandId(wishlistEntity.getProduct().getBrand().getId());
            wishlistDTO.setProductId(productDTO);
            list.add(wishlistDTO);
        }
        return list;
    }

    @Override
    public WishlistDTO findByUserIdAndProductId(int userId, int productId){
        WishlistDTO wishlistDTO = new WishlistDTO();
        WishlistEntity wishlistEntity = wishlistRepository.findByProduct_ProductIdAndAndUser_UserId(userId, productId).orElse(null);
        if(wishlistEntity != null){
            wishlistDTO.setUserId(wishlistEntity.getUser().getUserId());
            ProductDTO productDTO = productDTOConverter.toProductDTO(wishlistEntity.getProduct());
            productDTO.setBrandId(wishlistEntity.getProduct().getBrand().getId());
            wishlistDTO.setProductId(productDTO);
            return wishlistDTO;
        }
        return null;
    }

    @Override
    public boolean addWishlist(WishlistDTO wishlistDTO) {
        try {
            ProductEntity product = productRepository.findById(wishlistDTO.getProductId().getProductId()).orElse(null);
            UserEntity user = userRepository.findById(wishlistDTO.getUserId()).orElse(null);

            if (product == null && user == null) {
                return false;
            }

            WishlistEntity item = WishlistEntity.builder()
                    .product(product)
                    .user(user)
                    .build();

            wishlistRepository.save(item);
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteWishlist(WishlistDTO wishlistDTO) {
        try {
            WishlistEntity wishlistEntity = wishlistRepository.findByProduct_ProductIdAndAndUser_UserId(wishlistDTO.getProductId().getProductId(), wishlistDTO.getUserId()).orElse(null);
            if (wishlistEntity != null) {
                wishlistRepository.delete(wishlistEntity);
                return true;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
