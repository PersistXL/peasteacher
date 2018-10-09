package com.peas.manager;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableDiscoveryClient
@EntityScan(basePackages={"com.peas.api.entity"})
@MapperScan("com.peas.manager.dao")
@EnableTransactionManagement
@EnableFeignClients
@ComponentScan(basePackages={"com.peas.manager","com.peas.common"})
public class PeasManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PeasManagerApplication.class, args);
    }
}
