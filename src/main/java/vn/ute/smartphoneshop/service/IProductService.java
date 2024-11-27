package vn.ute.smartphoneshop.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import vn.ute.smartphoneshop.entity.ProductEntity;
import vn.ute.smartphoneshop.model.dto.ProductDTO;

import java.util.List;
import java.util.Map;

public interface IProductService {
//    List<ProductDTO>findAll(Map<String,Object> params);
//    List<ProductDTO> findAllProduct();
    Page<ProductDTO> findAll(Map<String, Object> params, Pageable pageable);
    Page<ProductDTO> findAllProduct(Pageable pageable);
    void saveProduct(ProductDTO product, MultipartFile file,String existingImagePath);
    ProductDTO findProductById(int id);
    void deleteProductById(int id);
    List<ProductDTO> findProductByBrandName(String brandName);

    List<ProductDTO> newArrivalProduct();
}
