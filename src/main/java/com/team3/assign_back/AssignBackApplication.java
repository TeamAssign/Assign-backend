package com.team3.assign_back;

import com.team3.assign_back.global.annotation.CustomMongoRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableCaching
@EnableScheduling
@EnableJpaRepositories(
        basePackages = "com.team3.assign_back",
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ANNOTATION,
                classes = CustomMongoRepository.class
        ))
@EnableJpaAuditing
@EnableAsync
@EnableWebSecurity
@SpringBootApplication
public class AssignBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(AssignBackApplication.class, args);
    }

}
