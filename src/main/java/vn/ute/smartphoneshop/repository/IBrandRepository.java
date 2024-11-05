package vn.ute.smartphoneshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ute.smartphoneshop.entity.BrandEntity;

@Repository
public interface IBrandRepository extends JpaRepository<BrandEntity,Long> {
    BrandEntity findByName(String name);
}
