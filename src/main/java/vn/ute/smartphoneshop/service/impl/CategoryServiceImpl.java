package vn.ute.smartphoneshop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import vn.ute.smartphoneshop.entity.CategoryEntity;
import vn.ute.smartphoneshop.repository.CategoryRepository;
import vn.ute.smartphoneshop.service.ICategoryService;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements ICategoryService {
    @Autowired
    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryEntity> findAll() {
        return this.categoryRepository.findAll();
    }

    public boolean update(CategoryEntity category) {
        try {
            this.categoryRepository.save(category);
            return true;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public CategoryEntity findById(int id) {
        return this.categoryRepository.findById(id).get();
    }
}
