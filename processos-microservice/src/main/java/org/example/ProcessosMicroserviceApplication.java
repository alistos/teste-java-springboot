package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "org.example")
@EnableJpaRepositories(basePackages = "org.example.config.db")
@ComponentScan(basePackages = { "org.example" })
@EntityScan(basePackages = "org.example.service")
public class ProcessosMicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProcessosMicroserviceApplication.class, args);
    }
}