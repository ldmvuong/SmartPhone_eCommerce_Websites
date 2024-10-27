package vn.ute.smartphoneshop.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import vn.ute.smartphoneshop.entity.UserEntity;
import vn.ute.smartphoneshop.model.dto.UserDTO;

import java.util.List;

public interface IUserService extends UserDetailsService {
    List<UserDTO> findAll();
    UserDTO findById(int id);
    boolean add(UserDTO user);
    boolean update(UserDTO user);
    boolean delete(int id);
    UserDTO findByUsername(String username);
}
