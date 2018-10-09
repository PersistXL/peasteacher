package com.peas.service;


import com.peas.service.service.impl.UserServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PeasServiceApplicationTests {

    @Autowired
    private UserServiceImpl userService;

    @Test
    public void contextLoads() throws Exception {
        userService.redis();
    }

}
