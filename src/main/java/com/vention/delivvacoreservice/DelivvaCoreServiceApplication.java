package com.vention.delivvacoreservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableFeignClients
@EnableScheduling
@EnableAsync
@EnableTransactionManagement
public class DelivvaCoreServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DelivvaCoreServiceApplication.class, args);
    }

}
