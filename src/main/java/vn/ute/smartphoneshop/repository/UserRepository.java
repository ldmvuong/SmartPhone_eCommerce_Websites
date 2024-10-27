package vn.ute.smartphoneshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ute.smartphoneshop.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Integer> {
//    void insertUser(UserEntity user);
//    void updateUser(UserEntity user);
//    UserEntity findByUsername(String username);
//    boolean existsByUsername(String username);
//    boolean existsByEmail(String email);
//    boolean existsByPhone(String phone);
    UserEntity findById(int id);
    UserEntity findByuserName(String username);
}
