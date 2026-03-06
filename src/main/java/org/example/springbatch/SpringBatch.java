package org.example.springbatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SpringBatch {

    public static void main(String[] args) {
        SpringApplication.run(SpringBatch.class, args);
    }
}