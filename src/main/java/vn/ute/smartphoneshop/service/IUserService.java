package vn.ute.smartphoneshop.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import vn.ute.smartphoneshop.entity.UserEntity;
import vn.ute.smartphoneshop.model.dto.UserDTO;

import java.util.List;

public interface IUserService extends UserDetailsService {
    List<UserDTO> findAll();
    Page<UserDTO> findAll(Pageable pageable);
    UserDTO findById(int id);
    boolean add(UserDTO user);
    UserDTO  findByEmail(String email);
    UserDTO findByUsername(String username);
    boolean update(UserDTO user);
    boolean delete(int id);
    boolean checkPassword(UserDTO user, String rawPassword);
    void updatePassword(int userId, String newPassword);
    UserEntity getUserById(int id);

    Long countUser();
}
