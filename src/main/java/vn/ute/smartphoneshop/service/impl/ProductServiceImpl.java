package vn.ute.smartphoneshop.service.impl;

import org.springframework.stereotype.Service;
import vn.ute.smartphoneshop.dtos.ProductDTO;
import vn.ute.smartphoneshop.service.ProductService;

import java.util.List;
import java.util.Map;
@Service
public class ProductServiceImpl implements ProductService {

    @Override
    public List<ProductDTO> findAll(Map<String, Object> params) {
        return List.of();
    }
}
