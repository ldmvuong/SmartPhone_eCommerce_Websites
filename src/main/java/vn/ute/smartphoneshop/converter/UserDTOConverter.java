package vn.ute.smartphoneshop.converter;

import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vn.ute.smartphoneshop.entity.RoleEntity;
import vn.ute.smartphoneshop.entity.UserEntity;
import vn.ute.smartphoneshop.exception.DataNotFoundException;
import vn.ute.smartphoneshop.model.dto.UserDTO;
import vn.ute.smartphoneshop.repository.RoleRepository;

@Component
public class UserDTOConverter {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private RoleRepository roleRepository;

    @PostConstruct
    public void init() {
        // Ánh xạ từ UserEntity sang UserDTO, lấy roleId từ RoleEntity
        modelMapper.typeMap(UserEntity.class, UserDTO.class).addMappings(mapper -> {
            mapper.map(src -> src.getRole().getRoleId(), UserDTO::setRoleId);
        });
    }

    public UserDTO toUserDTO(UserEntity item) {
        UserDTO userDTO = modelMapper.map(item, UserDTO.class);
        return userDTO;
    }

    public UserEntity toUserEntity(UserDTO userDTO) {
        UserEntity userEntity = modelMapper.map(userDTO, UserEntity.class);

        // Ánh xạ roleId từ DTO thành đối tượng RoleEntity
        RoleEntity role = roleRepository.findById(userDTO.getRoleId()).orElseThrow(() -> new DataNotFoundException("Invalid role ID: " + userDTO.getRoleId()));
        userEntity.setRole(role);
        return userEntity;
    }
}
