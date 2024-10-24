package vn.ute.smartphoneshop.converter;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vn.ute.smartphoneshop.entity.RoleEntity;
import vn.ute.smartphoneshop.model.dto.RoleDTO;

@Component
public class RoleDTOConverter {
    @Autowired
    private ModelMapper modelMapper;

    public RoleDTO convertRoleEntityToRoleDTO(RoleEntity roleEntity) {
        RoleDTO roleDTO = modelMapper.map(roleEntity, RoleDTO.class);
        return roleDTO;
    }
}
