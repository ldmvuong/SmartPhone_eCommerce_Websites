package vn.ute.smartphoneshop.converter;

import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vn.ute.smartphoneshop.entity.UserEntity;
import vn.ute.smartphoneshop.model.dto.UserDTO;

@Component
public class UserDTOConverter {

    @Autowired
    private ModelMapper modelMapper;

    @PostConstruct
    public void init() {
        // Ánh xạ từ UserEntity sang UserDTO, lấy roleName từ RoleEntity
        modelMapper.typeMap(UserEntity.class, UserDTO.class).addMappings(mapper -> {
            mapper.map(src -> src.getRole().getRoleId(), UserDTO::setRoleId);
        });
    }

    public UserDTO toUserDTO(UserEntity item) {
        UserDTO userDTO = modelMapper.map(item, UserDTO.class);
        return userDTO;
    }
}
