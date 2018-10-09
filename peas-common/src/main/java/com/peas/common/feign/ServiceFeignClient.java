package com.peas.common.feign;


import com.peas.api.entity.service.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "service")
@Component
public interface ServiceFeignClient {


    @GetMapping("/user")
    String findAll();

    @PutMapping("/user")
    String update(@RequestBody User user);
}
