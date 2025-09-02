package com.darkness.userService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(value = {"com.darkness.userService.*", "com.darkness.commons.security.*"})
public class UserServiceApplication {
    public static void main(String []args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}
