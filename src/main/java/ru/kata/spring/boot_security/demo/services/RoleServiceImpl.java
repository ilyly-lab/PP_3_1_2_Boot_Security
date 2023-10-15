package ru.kata.spring.boot_security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.RoleRepositoryEntity;

import java.util.List;
@Service
public class RoleServiceImpl implements RoleService{

    private RoleRepository roleRepository;
    private RoleRepositoryEntity roleRepositoryentity;
    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository, RoleRepositoryEntity roleRepositoryentity) {
        this.roleRepository = roleRepository;
        this.roleRepositoryentity = roleRepositoryentity;
    }


    @Override
    @Transactional(readOnly=true)
    public List<Role> getAllRoll() {
        return roleRepository.findAll();
    }

    @Override
    @Transactional(readOnly=true)
    public Role findByRoleId(long id) {
        return roleRepository.getById(id);
    }
    @Override
    @Transactional
    public void save(Role role) {
        roleRepository.save(role);
    }

    @Override
    @Transactional(readOnly=true)
    public Role getRoleByName(String name) {
        return roleRepositoryentity.getRoleByName(name);
    }
}
