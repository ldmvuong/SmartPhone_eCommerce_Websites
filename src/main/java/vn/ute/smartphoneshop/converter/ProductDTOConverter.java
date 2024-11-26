package vn.ute.smartphoneshop.converter;

import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vn.ute.smartphoneshop.model.dto.ProductDTO;
import vn.ute.smartphoneshop.entity.ProductEntity;
import vn.ute.smartphoneshop.repository.IBrandRepository;

@Component
public class ProductDTOConverter {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private IBrandRepository brandRepository;

    @PostConstruct
    public void init() {
        // Mapping from ProductEntity to ProductDTO, extracting brandId from BrandEntity
        modelMapper.typeMap(ProductEntity.class, ProductDTO.class).addMappings(mapper -> {
            mapper.map(src -> src.getBrand().getId(), ProductDTO::setBrandId);
        });
    }

    public ProductDTO toProductDTO(ProductEntity productEntity) {
        ProductDTO productDTO = modelMapper.map(productEntity, ProductDTO.class);
        if (productEntity.getBrand() != null) {
            productDTO.setBrandName(productEntity.getBrand().getName());
        }
        return productDTO;
    }

    public ProductEntity toProductEntity(ProductDTO productDTO) {
        ProductEntity productEntity = modelMapper.map(productDTO, ProductEntity.class);
        return productEntity;
    }

}
