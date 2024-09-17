package vn.ute.smartphoneshop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.ute.smartphoneshop.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity,Integer> {
}
