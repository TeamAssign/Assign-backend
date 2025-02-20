package com.team3.assign_back.global.config;

import com.auth0.client.auth.AuthAPI;
import com.auth0.client.mgmt.ManagementAPI;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.auth.TokenHolder;
import com.auth0.net.TokenRequest;
import com.team3.assign_back.global.exception.ErrorCode;
import com.team3.assign_back.global.exception.custom.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class Auth0Config {
    @Value("${AUTH0_DOMAIN}")
    private String domain;

    @Value("${AUTH0_CLIENT_ID}")
    private String clientId;

    @Value("${AUTH0_CLIENT_SECRET}")
    private String clientSecret;

    @Value("${AUTH0_AUDIENCE}")
    private String audience;

    @Bean
    public ManagementAPI managementAPI() {
        try {
            log.info("Auth0Config: Access Token 요청 시작");

            AuthAPI authAPI = AuthAPI.newBuilder(domain, clientId, clientSecret).build();
            TokenRequest tokenRequest = authAPI.requestToken(audience);
            TokenHolder holder = tokenRequest.execute().getBody();

            if (holder == null || holder.getAccessToken() == null) {
                log.error("Access Token을 가져오지 못했습니다.");
                throw new CustomException(ErrorCode.AUTH0_API_ERROR);
            }
            log.warn("Auth0 Access Token: {}", holder.getAccessToken());

            return ManagementAPI.newBuilder(domain, holder.getAccessToken()).build();
        } catch (Auth0Exception e) {
            log.error("Auth0 API 호출 중 오류 발생: {}", e.getMessage(), e);
            throw new CustomException(ErrorCode.AUTH0_API_ERROR);
        }
    }
}