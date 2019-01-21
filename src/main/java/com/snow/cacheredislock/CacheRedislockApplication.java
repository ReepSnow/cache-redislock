package com.snow.cacheredislock;

import com.snow.cacheredislock.api.CacheKeyGenerator;
import com.snow.cacheredislock.api.impl.LockKeyGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CacheRedislockApplication {

    public static void main(String[] args) {
        SpringApplication.run(CacheRedislockApplication.class, args);
    }
    @Bean
    public CacheKeyGenerator cacheKeyGenerator() {
        return new LockKeyGenerator();
    }
}

