package vn.ute.smartphoneshop.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import vn.ute.smartphoneshop.model.dto.UserDTO;

import java.util.List;

public interface IUserService extends UserDetailsService {
    List<UserDTO> findAll();
    UserDTO findById(int id);
    boolean add(UserDTO user);
    UserDTO  findByEmail(String email);
    UserDTO findByUsername(String username);
    boolean update(UserDTO user);
    boolean delete(int id);
    boolean checkPassword(UserDTO user, String rawPassword);
    void updatePassword(int userId, String newPassword);
}
