package com.example.providerb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication(scanBasePackages = "com.example.*")
@EnableEurekaClient
public class ProviderBApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProviderBApplication.class, args);
    }

}
