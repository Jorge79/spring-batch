package org.example.springbatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBatch {

    // Inicializa o Spring Boot e dispara o job configurado.
    public static void main(String[] args) {
        SpringApplication.run(SpringBatch.class, args);
    }

}