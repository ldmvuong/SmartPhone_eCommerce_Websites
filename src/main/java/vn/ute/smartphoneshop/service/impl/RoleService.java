package vn.ute.smartphoneshop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.ute.smartphoneshop.repository.RoleRepository;
import vn.ute.smartphoneshop.service.IRoleService;

@Service
public class RoleService implements IRoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public boolean findById(int id) {
        return roleRepository.findById(id).isPresent();
    }
}
