package com.peas.manager.service;

import com.peas.api.entity.manager.Role;

import java.util.List;

public interface RoleService {

    Role insert(Role role);

    List<Role> findAll();
}
