package vn.ute.smartphoneshop.service;

import vn.ute.smartphoneshop.entity.ProductEntity;
import vn.ute.smartphoneshop.model.dto.ProductDTO;

import java.util.List;
import java.util.Map;

public interface ProductService {
    List<ProductDTO>findAll(Map<String,Object> params);
    List<ProductDTO> findAllProduct();
    void save(ProductDTO product);

}
