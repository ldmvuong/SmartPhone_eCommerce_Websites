package vn.ute.smartphoneshop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.ute.smartphoneshop.builder.ProductSearchBuilder;
import vn.ute.smartphoneshop.converter.ProductDTOConverter;
import vn.ute.smartphoneshop.converter.ProductSearchBuilderConverter;
import vn.ute.smartphoneshop.model.dto.ProductDTO;
import vn.ute.smartphoneshop.entity.ProductEntity;
import vn.ute.smartphoneshop.repository.ProductRepository;
import vn.ute.smartphoneshop.repository.custom.ProductRepositoryCustom;
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
    public List<ProductDTO> findAllProduct() {
        List<ProductEntity> productEntities =productRepository.findAll();

        List<ProductDTO> result = new ArrayList<ProductDTO>();
        for (ProductEntity item : productEntities) {
            ProductDTO building = productDTOConverter.toProductDTO(item);
            result.add(building);
        }
        return result;
    }

    @Override
    public void save(ProductDTO product) {
        if (product.getStockQuantity() > 0) {
            product.setStatus("Còn hàng");
        } else {
            product.setStatus("Hết hàng");
        }
        ProductEntity productEntity = productDTOConverter.toProductEntity(product);
        productRepository.save(productEntity);
    }

    @Override
    public ProductDTO findProductById(int id) {
        ProductDTO productDTO = productDTOConverter.toProductDTO(productRepository.findById(id).get());
        return productDTO;
    }

    @Override
    public void deleteProductById(int id) {
        productRepository.deleteById(id);
    }
}
