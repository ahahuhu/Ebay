package com.soft.demo;

import org.dromara.x.file.storage.spring.EnableFileStorage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableFileStorage
public class Demo0629Application {

    public static void main(String[] args) {
        SpringApplication.run(Demo0629Application.class, args);
    }

}
