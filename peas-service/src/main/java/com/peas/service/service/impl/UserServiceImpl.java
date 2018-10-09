package com.peas.service.service.impl;


import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.peas.api.entity.service.User;
import com.peas.common.base.BaseServiceImpl;
import com.peas.common.util.RedisUtil;
import com.peas.service.dao.UserMapper;
import com.peas.service.dao.UserRepository;

import com.peas.service.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Map;

@Service("userService")
@Transactional
@Slf4j
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {

    private UserRepository userRepository;

    private UserMapper userMapper;


    @Autowired
    private RedisUtil redisUtil;



    @Autowired
    private void setUserRepository(UserRepository userRepository){
        super.setRepository(userRepository);
        this.userRepository = userRepository;
    }

    @Autowired
    private void setUserMapper(UserMapper userMapper){
        this.userMapper = userMapper;
    }


    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findByIdMapper(String id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public int deleteByMapperId(String id) {
        return userMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int update(User user) {
        return userMapper.updateByPrimaryKeySelective(user);
    }

   // @Transactional(rollbackFor = Exception.class)
    public void redis() throws Exception {

        //操作字符串
        redisUtil.set("string","字符串");
        Object string = redisUtil.get("string");
        log.info("string：{}",string);


        Map<String,Object> map = Maps.newHashMap();
        map.put("map1","1");
        map.put("map2","2");
        map.put("map3","3");
        redisUtil.hmset("hash",map);//操作hash
        Map<Object, Object> hash = redisUtil.hmget("hash");
        log.info("hash：{}",hash);

        List<String> list = Lists.newArrayList();
        list.add("list1");
        list.add("list2");
        list.add("list3");
        //redisTemplate.opsForList().rightPushAll("list",list);//操作list
        redisUtil.lSet("list",list);
       // log.info("list：{}",list1);
        // 测试事物
       // throw new RuntimeException();
       // redisTemplate.opsForSet();//操作set

      //  redisTemplate.opsForZSet();//操作有序set
    }

}
