package com.example.myfish;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication(scanBasePackages = "com.*")
public class MyfishApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyfishApplication.class, args);
    }


    @Bean
    public RestTemplate restTemplate(){

        return new RestTemplate();
    }

}
