package vn.ute.smartphoneshop.service;

import org.springframework.web.multipart.MultipartFile;
import vn.ute.smartphoneshop.entity.ProductEntity;
import vn.ute.smartphoneshop.model.dto.ProductDTO;

import java.util.List;
import java.util.Map;

public interface IProductService {
    List<ProductDTO>findAll(Map<String,Object> params);
    List<ProductDTO> findAllProduct();
    void saveProduct(ProductDTO product, MultipartFile file,String existingImagePath);
    ProductDTO findProductById(int id);
    void deleteProductById(int id);
    List<ProductEntity> findProductByBrandName(String brandName);
}
