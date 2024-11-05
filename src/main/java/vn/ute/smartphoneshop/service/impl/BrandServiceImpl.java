package vn.ute.smartphoneshop.service.impl;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.ute.smartphoneshop.entity.BrandEntity;
import vn.ute.smartphoneshop.model.dto.BrandDTO;
import vn.ute.smartphoneshop.repository.IBrandRepository;
import vn.ute.smartphoneshop.service.IBrandService;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class BrandServiceImpl implements IBrandService {
    @Autowired
    IBrandRepository brandRepository;

    private final Path root = Paths.get("./uploads");

    @Override
    public void init() {
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    public String saveImage(MultipartFile file) {
        try {
            // get file name
            String fileName = file.getOriginalFilename();
            // generate code random base on UUID
            String uniqueFileName = UUID.randomUUID().toString() + "_" + LocalDate.now() + "_" + fileName;
            Files.copy(file.getInputStream(), this.root.resolve(uniqueFileName), StandardCopyOption.REPLACE_EXISTING);
            return uniqueFileName;
        } catch (Exception e) {
            if (e instanceof FileAlreadyExistsException) {
                throw new RuntimeException("Filename already exists.");
            }

            throw new RuntimeException(e.getMessage());
        }
    }

    public String updateImage(MultipartFile file, String filename) {
        try {
            if (deleteImage(filename)) {
                return saveImage(file);
            }
        } catch (Exception e) {
            if (e instanceof FileAlreadyExistsException) {
                throw new RuntimeException("Filename already exists.");
            }
            throw new RuntimeException(e.getMessage());
        }
        return saveImage(file);
    }

    private boolean isValidSuffixImage(String img) {
        return img.endsWith(".jpg") || img.endsWith(".jpeg") ||
                img.endsWith(".png") || img.endsWith(".gif") ||
                img.endsWith(".bmp");
    }

    @Override
    public boolean deleteImage(String filename) {
        try {
            Path file = root.resolve(filename);
            return Files.deleteIfExists(file);
        } catch (IOException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public List<BrandEntity> findAll(){
        return brandRepository.findAll();
    }

    @Override
    public boolean addBrand(BrandDTO brandDTO, MultipartFile file) throws IOException{
        try {
            String img = "";
            if(file == null){
                img = "/uploads/default-product.jpg";
            }
            else {
                if(!isValidSuffixImage(Objects.requireNonNull(file.getOriginalFilename()))){
                    throw new BadRequestException("Invalid suffix image");
                }
                img = saveImage(file);
            }
            BrandEntity brandEntity = new BrandEntity();
            BeanUtils.copyProperties(brandDTO, brandEntity);
            brandEntity.setImg(img);
            brandRepository.save(brandEntity);
            return true;
        }catch (IOException e){
            throw new IOException("Could not add brand: " + e.getMessage());
        }
    }

    @Override
    public boolean updateBrand(Long brandId, BrandDTO brandDTO, String oldImg, MultipartFile file) throws IOException{
        String img = "";
        if(file == null){
            img = "/uploads/default-product.jpg";
        }
        else {
            if(!isValidSuffixImage(Objects.requireNonNull(file.getOriginalFilename()))){
                throw new BadRequestException("Invalid suffix image");
            }
            img = updateImage(file, oldImg);
        }
        BrandEntity brand = new BrandEntity();
        BeanUtils.copyProperties(brandDTO, brand);
        brand.setImg(img);
        brand.setId(brandId);
        return brandRepository.save(brand) != null;
    }

    @Override
    public boolean deleteBrand(Long brandId){
        try {
            BrandEntity brand = brandRepository.findById(brandId).get();
            deleteImage(brand.getImg());
            brandRepository.delete(brand);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public BrandEntity findBrandByName(String brandName) {
        return brandRepository.findByName(brandName);
    }

    @Override
    public Optional<BrandEntity> findBrandById(Long id){
        return brandRepository.findById(id);
    }
}
