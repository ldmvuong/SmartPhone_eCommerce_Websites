package vn.ute.smartphoneshop.converter;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vn.ute.smartphoneshop.model.dto.ProductDTO;
import vn.ute.smartphoneshop.entity.ProductEntity;

@Component
public class ProductDTOConverter {
    @Autowired
    private ModelMapper modelMapper;

    public ProductDTO toProductDTO(ProductEntity productEntity) {
        ProductDTO productDTO = modelMapper.map(productEntity, ProductDTO.class);
        return productDTO;
    }

    public ProductEntity toProductEntity(ProductDTO productDTO) {
        ProductEntity productEntity = modelMapper.map(productDTO, ProductEntity.class);
        return productEntity;
    }
}
