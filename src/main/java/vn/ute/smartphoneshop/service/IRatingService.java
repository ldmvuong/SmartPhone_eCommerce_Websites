package vn.ute.smartphoneshop.service;

import vn.ute.smartphoneshop.entity.RatingEntity;
import vn.ute.smartphoneshop.model.dto.RatingDTO;

import java.util.List;

public interface IRatingService {
    List<RatingEntity> findByProductId(int productId);
    int countUser(int productId);
    float countRatingStar(int productId);
    boolean insert(RatingDTO ratingDTO);
    boolean checkOrderFirst(int productId, int userId);
}
