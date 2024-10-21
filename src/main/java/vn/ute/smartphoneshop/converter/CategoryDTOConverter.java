package vn.ute.smartphoneshop.converter;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vn.ute.smartphoneshop.entity.CategoryEntity;
import vn.ute.smartphoneshop.entity.ProductEntity;
import vn.ute.smartphoneshop.model.dto.CartDTO;
import vn.ute.smartphoneshop.model.dto.CategoryDTO;
import vn.ute.smartphoneshop.model.dto.ProductDTO;

@Component
public class CategoryDTOConverter {
    @Autowired
    private ModelMapper modelMapper;

    public CategoryDTO tocategoryDTO(CategoryEntity item) {
        CategoryDTO category = modelMapper.map(item, CategoryDTO.class);
        return category;
    }
}
