package vn.ute.smartphoneshop.service;

import vn.ute.smartphoneshop.entity.UserEntity;
import vn.ute.smartphoneshop.model.dto.UserDTO;

import java.util.List;

public interface IUserService {
    List<UserDTO> findAll();
    UserDTO findById(int id);
    boolean add(UserDTO user);
    boolean update(UserDTO user);
    boolean delete(int id);
}
