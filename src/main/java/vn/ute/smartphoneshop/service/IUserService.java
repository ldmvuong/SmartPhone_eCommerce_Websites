package vn.ute.smartphoneshop.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import vn.ute.smartphoneshop.entity.UserEntity;
import vn.ute.smartphoneshop.model.dto.UserDTO;

import java.util.List;
import java.util.Optional;

public interface IUserService extends UserDetailsService {
    List<UserDTO> findAll();
    UserDTO findById(int id);
    boolean add(UserDTO user);
    boolean findByEmail(String email);
    boolean findByUsername(String username);
    boolean update(UserDTO user);
    boolean delete(int id);
}
