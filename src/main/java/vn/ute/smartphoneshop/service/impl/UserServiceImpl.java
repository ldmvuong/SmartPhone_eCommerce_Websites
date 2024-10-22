package vn.ute.smartphoneshop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.ute.smartphoneshop.converter.UserDTOConverter;
import vn.ute.smartphoneshop.entity.UserEntity;
import vn.ute.smartphoneshop.model.dto.UserDTO;
import vn.ute.smartphoneshop.repository.UserRepository;
import vn.ute.smartphoneshop.service.IUserService;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserDTOConverter userDTOConverter;


    @Override
    public List<UserDTO> findAll() {
        List<UserEntity> list = userRepository.findAll();

        List<UserDTO> userDTOList = new ArrayList<UserDTO>();
        for (UserEntity userEntity : list) {
            UserDTO convert = userDTOConverter.toUserDTO(userEntity);
            userDTOList.add(convert);
        }
        return userDTOList;
    }

    @Override
    public UserDTO findById(int id) {
        UserEntity user = userRepository.findById(id);
        if (user != null) {
            return userDTOConverter.toUserDTO(user);
        }
        return null;
    }

    @Override
    public boolean add(UserEntity user) {
        return false;
    }

    @Override
    public boolean update(UserEntity user) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }
}
