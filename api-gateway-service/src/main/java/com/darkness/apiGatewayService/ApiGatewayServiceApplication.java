package com.darkness.apiGatewayService;

import com.darkness.apiGatewayService.config.GatewayCorsConfig;
import com.darkness.commons.security.config.SecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import java.security.SecureRandom;
import java.util.Base64;

@SpringBootApplication
@Import(value = {SecurityConfig.class,
        GatewayCorsConfig.class})
@ComponentScan(value = {"com.darkness.commons.security.*",
        "com.darkness.redisService.*"})
public class ApiGatewayServiceApplication {
    public static void main(String []args) {
        generateKey();
        SpringApplication.run(ApiGatewayServiceApplication.class, args);
    }

    private static void generateKey() {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[32];
        random.nextBytes(key);
        String secret = Base64.getEncoder().encodeToString(key);
        System.out.println("Generated Secret Key: " + secret);
    }
}
