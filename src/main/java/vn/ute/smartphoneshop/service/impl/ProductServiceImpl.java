package vn.ute.smartphoneshop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.ute.smartphoneshop.builder.ProductSearchBuilder;
import vn.ute.smartphoneshop.converter.ProductDTOConverter;
import vn.ute.smartphoneshop.converter.ProductSearchBuilderConverter;
import vn.ute.smartphoneshop.entity.BrandEntity;
import vn.ute.smartphoneshop.model.dto.ProductDTO;
import vn.ute.smartphoneshop.entity.ProductEntity;
import vn.ute.smartphoneshop.repository.IBrandRepository;
import vn.ute.smartphoneshop.repository.ProductRepository;
import vn.ute.smartphoneshop.repository.custom.ProductRepositoryCustom;
import vn.ute.smartphoneshop.service.IProductService;
import vn.ute.smartphoneshop.utils.ImageUtil;

import java.io.IOException;
import java.util.*;

import static vn.ute.smartphoneshop.utils.ImageUtil.deleteImage;

@Service
public class ProductServiceImpl implements IProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductDTOConverter productDTOConverter;
    @Autowired
    private IBrandRepository brandRepository;
    @Autowired
    private ProductSearchBuilderConverter productSearchBuilderConverter;


//    @Override
//    public List<ProductDTO> findAll(Map<String, Object> params) {
//        // chuyển đổi Map params thành ProductSearchBuilder
//        ProductSearchBuilder builder = productSearchBuilderConverter.toProductSearchBuilder(params);
//        List<ProductEntity> productEntities = productRepository.findAll(ProductRepositoryCustom.search(builder));
//
//        List<ProductDTO> result = new ArrayList<ProductDTO>();
//        for (ProductEntity item : productEntities) {
//            ProductDTO building = productDTOConverter.toProductDTO(item);
//            result.add(building);
//        }
//        return result;
//    }

    @Override
    public Page<ProductDTO> findAll(Map<String, Object> params, Pageable pageable) {
        // Chuyển đổi Map params thành ProductSearchBuilder
        ProductSearchBuilder builder = productSearchBuilderConverter.toProductSearchBuilder(params);

        Page<ProductEntity> productEntities = productRepository.findAll(
                ProductRepositoryCustom.search(builder), pageable
        );

        return productEntities.map(productDTOConverter::toProductDTO);
    }

//    @Override
//    public List<ProductDTO> findAllProduct() {
//        List<ProductEntity> productEntities =productRepository.findAll();
//
//        List<ProductDTO> result = new ArrayList<ProductDTO>();
//        for (ProductEntity item : productEntities) {
//            ProductDTO building = productDTOConverter.toProductDTO(item);
//            result.add(building);
//        }
//        return result;
//    }

    @Override
    public Page<ProductDTO> findAllProduct(Pageable pageable) {
        Page<ProductEntity> productEntities = productRepository.findAll(pageable);

        return productEntities.map(productDTOConverter::toProductDTO);
    }


    @Override
    public void saveProduct(ProductDTO product, MultipartFile file, String existingImagePath) {
        String img = (product.getImagePath() != null && !product.getImagePath().isEmpty()) ? product.getImagePath() : "default-product.jpg";

        try {
            // Kiểm tra nếu có ảnh mới được tải lên
            if (file != null && !file.isEmpty()) {
                // Nếu có ảnh cũ và không phải ảnh mặc định, xóa ảnh cũ
                if (existingImagePath != null && !existingImagePath.isEmpty() && !existingImagePath.equals("default-product.jpg")) {
                    deleteImage(existingImagePath); // Phương thức để xóa ảnh
                }
                // Lưu ảnh mới
                if (ImageUtil.isValidSuffixImage(Objects.requireNonNull(file.getOriginalFilename()))) {
                    img = ImageUtil.saveImage(file); // Lưu ảnh mới và cập nhật đường dẫn
                } else {
                    throw new IllegalArgumentException("Invalid image format. Only JPG, JPEG, PNG, GIF, BMP are allowed.");
                }
            }

            // Gán lại giá trị đường dẫn ảnh (mới hoặc cũ)
            product.setImagePath(img);

            if (product.getStockQuantity() > 0) {
                product.setStatus("Còn hàng");
            } else {
                product.setStatus("Hết hàng");
            }

            Optional<BrandEntity> brand = brandRepository.findById(product.getBrandId());
            ProductEntity productEntity = productDTOConverter.toProductEntity(product);
            productEntity.setBrand(brand.orElseThrow(() -> new RuntimeException("Brand not found")));
            productRepository.save(productEntity);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save the image: " + e.getMessage(), e);
        }
    }


    @Override
    public ProductDTO findProductById(int id) {
        return productRepository.findById(id)
                .map(productEntity -> {
                    ProductDTO productDTO = productDTOConverter.toProductDTO(productEntity);
                    productDTO.setBrandId(productEntity.getBrand().getId());
                    return productDTO;
                })
                .orElse(null);
    }

    @Override
    public void deleteProductById(int id) {
        try {
            ProductEntity product = productRepository.findById(id).get();
            deleteImage(product.getImagePath());
            productRepository.delete(product);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public List<ProductDTO> findProductByBrandName(String brandName) {
        List<ProductEntity> productEntities = productRepository.findByBrandName(brandName);
        List<ProductDTO> productDTOList = new ArrayList<>();
        for (ProductEntity productEntity : productEntities) {
            ProductDTO productDTO = productDTOConverter.toProductDTO(productEntity);
            productDTOList.add(productDTO);
        }
        return productDTOList;
    }

    @Override
    public List<ProductDTO> newArrivalProduct() {
        List<ProductEntity> productEntities = productRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        List<ProductDTO> productDTOList = new ArrayList<>();
        for (ProductEntity productEntity : productEntities){
            ProductDTO productDTO = productDTOConverter.toProductDTO(productEntity);
            productDTOList.add(productDTO);
        }
        return productDTOList;
    }

}
