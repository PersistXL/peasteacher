package com.peas.manager.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.peas.api.entity.manager.Role;
import com.peas.api.entity.service.User;
import com.peas.common.feign.ServiceFeignClient;
import com.peas.common.response.ServerResponse;
import com.peas.common.util.Json;
import com.peas.common.util.JsonList;
import com.peas.common.util.UUIDUtil;
import com.peas.manager.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/role")
@Slf4j
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ServiceFeignClient serviceFeignClient;


    @PostMapping("/init")
    public ServerResponse init(@RequestBody Role role){
        role.setId(UUIDUtil.randomUUIDw());
        Role insert = roleService.insert(role);
        String roleId = insert.getId();
        log.info("roleid:{}",roleId);
        String url = "http://SERVICE/user";
        String forObject = restTemplate.getForObject(url,String.class);
        Map<String, Object> stringObjectMap = Json.newJsonMap(forObject).toMap();
        List<JSONObject> data = Json.deserialize(Objects.toString(stringObjectMap.get("data")), List.class);
        data.forEach(s -> {
            User deserialize = Json.deserialize(s.toString(), User.class);
            deserialize.setRoleId(roleId);
            serviceFeignClient.update(deserialize);
        });
        return ServerResponse.createBySuccess();
    }
}
