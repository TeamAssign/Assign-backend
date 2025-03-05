package com.team3.assign_back.global.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.team3.assign_back.global.annotation.CustomMongoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.WriteResultChecking;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableMongoRepositories(
        basePackages = "com.team3.assign_back",
        includeFilters = @ComponentScan.Filter(
                type = FilterType.ANNOTATION,
                classes = CustomMongoRepository.class
        ),
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ANNOTATION,
                classes = {Repository.class}
        )
)
@EnableMongoAuditing
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Value("${spring.data.mongodb.uri}") // URL 환경 변수로 관리
    private String mongoUri;

    @Value("${spring.data.mongodb.database}") // 나의 DB
    private String database;

    @Override
    protected String getDatabaseName() {
        return database;
    }

    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create(
                MongoClientSettings.builder()
                        .applyConnectionString(new ConnectionString(mongoUri))
                        .applyToConnectionPoolSettings(builder ->    //여러 연결의 개수, 유지 및 관리 (여러 요청 동시 처리 설정)
                                builder
                                        .maxSize(50)  // 최대 연결 수
                                        .minSize(5)   // 최소 연결 수
                                        .maxWaitTime(10000, TimeUnit.MILLISECONDS)  // 연결 대기 시간
                                        .maxConnectionIdleTime(5, TimeUnit.MINUTES) // 유휴 연결 최대 시간
                        )
                        .applyToSocketSettings(builder -> // //하나의 연결에서 데이터를 주고받는 네트워크 연결 관리  (송수신 속도)
                                builder
                                        .connectTimeout(5000, TimeUnit.MILLISECONDS) // 연결 시도 최대 시간
                                        .readTimeout(3000, TimeUnit.MILLISECONDS)    // 읽기 작업 최대 대기 시간
                        )
                        .build()
        );
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        MongoTemplate template = new MongoTemplate(mongoClient(), getDatabaseName());
        template.setWriteConcern(WriteConcern.W1);
        template.setReadPreference(ReadPreference.secondaryPreferred());
        template.setWriteResultChecking(WriteResultChecking.EXCEPTION);
        return template;
    }
}