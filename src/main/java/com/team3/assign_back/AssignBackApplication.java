package com.team3.assign_back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;


@EnableJpaAuditing
@EnableAsync
@SpringBootApplication
public class AssignBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(AssignBackApplication.class, args);
    }

}
