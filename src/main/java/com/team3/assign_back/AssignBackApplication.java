package com.team3.assign_back;

import com.team3.assign_back.global.annotation.CustomMongoRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableJpaRepositories(
        basePackages = "com.team3.assign_back",
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ANNOTATION,
                classes = CustomMongoRepository.class
        ))
@SpringBootApplication
public class AssignBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(AssignBackApplication.class, args);
    }

}
