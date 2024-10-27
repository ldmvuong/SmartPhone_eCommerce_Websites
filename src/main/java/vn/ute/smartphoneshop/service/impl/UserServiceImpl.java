package vn.ute.smartphoneshop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vn.ute.smartphoneshop.converter.UserDTOConverter;
import vn.ute.smartphoneshop.entity.RoleEntity;
import vn.ute.smartphoneshop.entity.UserEntity;
import vn.ute.smartphoneshop.model.dto.UserDTO;
import vn.ute.smartphoneshop.repository.UserRepository;
import vn.ute.smartphoneshop.service.IUserService;

import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
    public boolean add(UserDTO user) {
        try{
            UserEntity userEntity = userDTOConverter.toUserEntity(user);
            userRepository.save(userEntity);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(UserDTO user) {
        try{
            UserEntity userEntity = userDTOConverter.toUserEntity(user);
            userRepository.save(userEntity);
            return true;
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

    @Override
    public UserDTO findByUsername(String username) {
        UserEntity userEntity = userRepository.findByuserName(username);
        if (userEntity != null) {
            return userDTOConverter.toUserDTO(userEntity);
        }
        return null;
    }

    private GrantedAuthority roleToAuthority(RoleEntity role) {
        return new SimpleGrantedAuthority(role.getRoleName());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByuserName(username);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        GrantedAuthority authority = roleToAuthority(user.getRole());
        return new org.springframework.security.core.userdetails.User(
                user.getUserName(),
                user.getPassword(),
                Collections.singletonList(authority)
        );
    }
}
