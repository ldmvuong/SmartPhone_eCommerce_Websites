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
        UserEntity user = userRepository.findByUserId(id);
        if (user != null) {
            return userDTOConverter.toUserDTO(user);
        }
        return null;
    }

    @Override
    public boolean add(UserDTO user) {
        try{
            if(this.findByEmail(user.getEmail()) == null&&this.findByUsername(user.getUserName()) == null){
                UserEntity userEntity = userDTOConverter.toUserEntity(user);
                userRepository.save(userEntity);
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public UserDTO findByEmail(String email) {
        if(userRepository.findByEmail(email).isPresent()){
            return userDTOConverter.toUserDTO(userRepository.findByEmail(email).get());
        }
        return null;
    }

    @Override
    public UserDTO findByUsername(String username) {
        if(userRepository.findByEmail(username).isPresent()){
            return userDTOConverter.toUserDTO(userRepository.findByUserName(username).get());
        }
        return null;
    }

    @Override
    public boolean update(UserDTO user) {
        try{
            if(this.findById(user.getUserId()) != null){
                UserEntity userEntity = userDTOConverter.toUserEntity(user);
                userRepository.save(userEntity);
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        try{
            userRepository.deleteById(id);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
