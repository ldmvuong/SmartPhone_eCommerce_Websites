package vn.ute.smartphoneshop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import vn.ute.smartphoneshop.converter.UserDTOConverter;
import vn.ute.smartphoneshop.entity.RoleEntity;
import vn.ute.smartphoneshop.entity.UserEntity;
import vn.ute.smartphoneshop.model.dto.MyUserDetail;
import vn.ute.smartphoneshop.model.dto.UserDTO;
import vn.ute.smartphoneshop.repository.UserRepository;
import vn.ute.smartphoneshop.service.IUserService;

import java.util.ArrayList;
import java.util.Collections;
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
    public Page<UserDTO> findAll(Pageable pageable) {
        Page<UserEntity> userEntityPage = userRepository.findAll(pageable);

        Page<UserDTO> userDTOPage = userEntityPage.map(userEntity ->
                userDTOConverter.toUserDTO(userEntity)
        );
        return userDTOPage;
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
        try {
            if (this.findByEmail(user.getEmail()) == null && this.findByUsername(user.getUserName()) == null) {
                UserEntity userEntity = userDTOConverter.toUserEntity(user);

                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                String encodedPassword = encoder.encode(user.getPassword());
                userEntity.setPassword(encodedPassword);
                userRepository.save(userEntity);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public UserDTO findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userDTOConverter::toUserDTO)
                .orElse(null);
    }

    @Override
    public UserDTO findByUsername(String username) {
        return userRepository.findByUserName(username)
                .map(userDTOConverter::toUserDTO)
                .orElse(null);
    }

    @Override
    public boolean update(UserDTO user) {
        try {
            if (this.findById(user.getUserId()) != null) {
                UserEntity userEntity = userDTOConverter.toUserEntity(user);
                userRepository.save(userEntity);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        try {
            userRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean checkPassword(UserDTO user, String rawPassword) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(rawPassword, user.getPassword());
    }

    @Override
    public void updatePassword(int userId, String newPassword) {
        UserDTO user = this.findById(userId);
        if (user != null) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String encodedPassword = encoder.encode(newPassword);
            user.setPassword(encodedPassword);
            userRepository.save(userDTOConverter.toUserEntity(user));
        }
    }

    @Override
    public UserEntity getUserById(int id) {
        return userRepository.findById(id).orElse(null);
    }

    private GrantedAuthority roleToAuthority(RoleEntity role) {
        return new SimpleGrantedAuthority(role.getRoleName());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid username or password."));

        GrantedAuthority authority = roleToAuthority(user.getRole());

        String fullName = user.getLastName() + " " + user.getFirstName();

        return new MyUserDetail(
                user.getUserName(),
                user.getPassword(),
                true,
                true,
                true,
                true,
                Collections.singletonList(authority),
                user.getUserId(),
                fullName
        );
    }

    @Override
    public Long countUser(){
        return userRepository.countUsers();
    }
}
