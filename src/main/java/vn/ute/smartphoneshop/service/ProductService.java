package vn.ute.smartphoneshop.service;

import vn.ute.smartphoneshop.dtos.ProductDTO;

import java.util.List;
import java.util.Map;

public interface ProductService {
    List<ProductDTO>findAll(Map<String,Object> params);
    ProductDTO findById(int id);
}
