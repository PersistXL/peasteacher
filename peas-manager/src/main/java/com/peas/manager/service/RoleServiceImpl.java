package com.peas.manager.service;

import com.peas.api.entity.manager.Role;
import com.peas.manager.dao.RoleMapper;
import com.peas.manager.dao.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RoleServiceImpl implements RoleService{


    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RoleRepository roleRepository;




    @Override
    public Role insert(Role role) {
        return roleRepository.saveAndFlush(role);
    }

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }
}
