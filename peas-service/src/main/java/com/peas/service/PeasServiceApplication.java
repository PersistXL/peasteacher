package com.peas.service;

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
@MapperScan("com.peas.service.dao")
@EnableTransactionManagement
@EnableFeignClients
@ComponentScan(basePackages={"com.peas.service","com.peas.common"})
public class PeasServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PeasServiceApplication.class, args);
    }
}
