package vn.ute.smartphoneshop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.ute.smartphoneshop.builder.ProductSearchBuilder;
import vn.ute.smartphoneshop.converter.ProductDTOConverter;
import vn.ute.smartphoneshop.converter.ProductSearchBuilderConverter;
import vn.ute.smartphoneshop.dtos.ProductDTO;
import vn.ute.smartphoneshop.entity.ProductEntity;
import vn.ute.smartphoneshop.repositories.ProductRepository;
import vn.ute.smartphoneshop.repositories.custom.ProductRepositoryCustom;
import vn.ute.smartphoneshop.service.ProductService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductDTOConverter productDTOConverter;
    @Autowired
    private ProductSearchBuilderConverter productSearchBuilderConverter;


    @Override
    public List<ProductDTO> findAll(Map<String, Object> params) {
        // chuyển đổi Map params thành ProductSearchBuilder
        ProductSearchBuilder builder = productSearchBuilderConverter.toProductSearchBuilder(params);
        List<ProductEntity> productEntities = productRepository.findAll(ProductRepositoryCustom.search(builder));

        List<ProductDTO> result = new ArrayList<ProductDTO>();
        for (ProductEntity item : productEntities) {
            ProductDTO building = productDTOConverter.toProductDTO(item);
            result.add(building);
        }
        return result;
    }

    @Override
    public ProductDTO findById(int id) {

        ProductDTO productDTO = productDTOConverter.toProductDTO(productRepository.findById(id).get());
        return productDTO;
    }
}
