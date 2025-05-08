package com.example.frontofficeapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("com.example.frontofficeapi.entity")
public class FrontOfficeApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(FrontOfficeApiApplication.class, args);
    }

}
