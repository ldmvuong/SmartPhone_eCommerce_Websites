package vn.ute.smartphoneshop.service;

import org.springframework.web.multipart.MultipartFile;
import vn.ute.smartphoneshop.entity.BrandEntity;
import vn.ute.smartphoneshop.model.dto.BrandDTO;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface IBrandService {
    void init();

    boolean deleteImage(String filename);

    List<BrandEntity> findAll();

    boolean addBrand(BrandDTO brandDTO, MultipartFile file) throws IOException;

    boolean updateBrand(Long brandId, BrandDTO brandDTO, String oldImg, MultipartFile file) throws IOException;

    boolean deleteBrand(Long brandId);

    BrandEntity findBrandByName(String brandName);

    Optional<BrandEntity> findBrandById(Long id);
}
