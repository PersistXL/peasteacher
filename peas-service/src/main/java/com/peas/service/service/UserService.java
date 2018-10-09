package com.peas.service.service;


import com.peas.api.entity.service.User;
import com.peas.common.base.BaseService;


import java.util.List;

public interface UserService extends BaseService<User> {

    List<User> findAll();


    User findByIdMapper(String id);

    int deleteByMapperId(String id);

    int update(User user);

}
