package com.darkness.userService;

import com.darkness.commons.security.config.SecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableMethodSecurity
@Import(value = {SecurityConfig.class})
@ComponentScan(value = {"com.darkness.userService.*",
        "com.darkness.redisService.*",
        "com.darkness.commons.*"})
public class UserServiceApplication {
    public static void main(String []args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}
