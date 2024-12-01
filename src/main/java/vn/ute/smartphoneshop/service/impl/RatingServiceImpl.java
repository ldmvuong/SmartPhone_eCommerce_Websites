package vn.ute.smartphoneshop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import vn.ute.smartphoneshop.entity.OrderDetailEntity;
import vn.ute.smartphoneshop.entity.ProductEntity;
import vn.ute.smartphoneshop.entity.RatingEntity;
import vn.ute.smartphoneshop.entity.UserEntity;
import vn.ute.smartphoneshop.model.dto.RatingDTO;
import vn.ute.smartphoneshop.repository.OrderDetailRepository;
import vn.ute.smartphoneshop.repository.ProductRepository;
import vn.ute.smartphoneshop.repository.RatingRepository;
import vn.ute.smartphoneshop.repository.UserRepository;
import vn.ute.smartphoneshop.service.IRatingService;

import java.util.List;
@Service
public class RatingServiceImpl implements IRatingService {

    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<RatingEntity> findByProductId(int productId) {
        return ratingRepository.findByProduct_ProductId(productId);
    }

    @Override
    public int countUser(int productId) {
        return findByProductId(productId).size();
    }

    @Override
    public float countRatingStar(int productId) {
        List<RatingEntity> ratings = ratingRepository.findByProduct_ProductId(productId);
        if (ratings.isEmpty())
            return 0;
        float sum = 0;
        for (RatingEntity rating : ratings) {
            sum += rating.getStar();
        }
        return sum / ratings.size();
    }

    @Override
    public boolean insert(RatingDTO ratingDTO) {
        try {
            ProductEntity product = productRepository.findById(ratingDTO.getProductId()).orElse(null);
            UserEntity user = userRepository.findById(ratingDTO.getUserId()).orElse(null);
            if(product == null||user == null){
                return false;
            }
            RatingEntity rating = new RatingEntity(ratingDTO.getContent(), ratingDTO.getStar(), user, product);
            ratingRepository.save(rating);

            product.setRating(this.countRatingStar(ratingDTO.getProductId()));
            productRepository.save(product);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean checkOrderFirst(int productId, int userId) {
        List<OrderDetailEntity> orderDetails = orderDetailRepository.findByProduct_ProductId(productId);

        if(orderDetails.size() > 0){
            for(OrderDetailEntity orderDetail : orderDetails){
                if (orderDetail.getOrder().getUser().getUserId() == userId){
                    return true;
                }
            }
        }
        return false;
    }
}
