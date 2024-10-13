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

    public ProductDTO toProductDTO(ProductEntity item) {
        ProductDTO product = modelMapper.map(item, ProductDTO.class);
        return product;
    }

}
