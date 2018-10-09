package com.peas.service.controller;


import com.peas.api.entity.service.User;
import com.peas.common.response.ServerResponse;
import com.peas.common.util.UUIDUtil;
import com.peas.hsf.tool.Checkers;
import com.peas.service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {


    private UserService userService;

    @Autowired
    private void setUserService(UserService userService){
        this.userService = userService;
    }


    @GetMapping
    public ServerResponse<List<User>> findAll(){
        List<User> all = userService.findAll();
        return ServerResponse.createBySuccess(all);
    }

    @PostMapping
    public ServerResponse insert(@RequestParam(required = false) User user){
        user.setId(UUIDUtil.randomUUIDw());
        userService.save(user);
        return ServerResponse.createBySuccess();
    }

    @GetMapping("{id}")
    public ServerResponse<User> findById(@PathVariable("id")String id){
        User one = userService.findOne(id);
        return ServerResponse.createBySuccess(one);
    }


    @GetMapping("mapper/{id}")
    public ServerResponse<User> findByIdMapper(@PathVariable("id")String id){
        User one = userService.findByIdMapper(id);
        return ServerResponse.createBySuccess(one);
    }

    @DeleteMapping("{id}")
    public ServerResponse<User> deleteById(@PathVariable("id")String id){
        Checkers.checkState(userService.deleteByMapperId(id)==1,"delete fail"); ;
        return ServerResponse.createBySuccess();
    }

    @PutMapping
    public ServerResponse update(@RequestBody User user){
        userService.update(user);
        return ServerResponse.createBySuccess();
    }


}
