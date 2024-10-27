package vn.ute.smartphoneshop.service;

import vn.ute.smartphoneshop.entity.UserEntity;
import vn.ute.smartphoneshop.model.dto.UserDTO;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    List<UserDTO> findAll();
    UserDTO findById(int id);
    boolean add(UserDTO user);
    UserDTO findByEmail(String email);
    UserDTO findByUsername(String username);
    boolean update(UserDTO user);
    boolean delete(int id);
}
