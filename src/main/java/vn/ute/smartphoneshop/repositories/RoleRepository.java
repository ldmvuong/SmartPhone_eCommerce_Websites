package vn.ute.smartphoneshop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.ute.smartphoneshop.entity.RoleEntity;

public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {
}
