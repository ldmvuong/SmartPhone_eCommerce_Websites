package vn.ute.smartphoneshop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.ute.smartphoneshop.converter.RoleDTOConverter;
import vn.ute.smartphoneshop.model.dto.RoleDTO;
import vn.ute.smartphoneshop.repository.RoleRepository;
import vn.ute.smartphoneshop.service.IRoleService;

@Service
public class RoleServiceImpl implements IRoleService {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RoleDTOConverter roleDTOConverter;

    @Override
    public boolean findById(int id) {
        return roleRepository.findById(id).isPresent();
    }

    @Override
    public RoleDTO findByRoleName(String name) {
        return roleDTOConverter.toRoleDTO(roleRepository.findByRoleName(name));
    }
}
