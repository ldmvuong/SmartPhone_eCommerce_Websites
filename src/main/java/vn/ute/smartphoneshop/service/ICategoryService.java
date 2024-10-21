package vn.ute.smartphoneshop.service;

import jdk.jfr.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import vn.ute.smartphoneshop.entity.CategoryEntity;

import java.util.List;

@Repository
public interface ICategoryService {
    public List<CategoryEntity> findAll();
    public boolean update(CategoryEntity category);
    public CategoryEntity findById(int id);
}
