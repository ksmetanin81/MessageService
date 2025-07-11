package com.colvir;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class PrepareServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(PrepareServiceApplication.class, args);
    }
}