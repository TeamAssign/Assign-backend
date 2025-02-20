package com.team3.assign_back.domain.users.service;

import com.auth0.client.mgmt.ManagementAPI;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.mgmt.users.User;
import com.team3.assign_back.global.exception.ErrorCode;
import com.team3.assign_back.global.exception.custom.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Auth0Service {
    private final ManagementAPI managementAPI;

    public User getUserById(String vendorId) {
        try {
            User user = managementAPI.users().get(vendorId, null).execute().getBody();

            if (user == null) {
                throw new CustomException(ErrorCode.USER_NOT_FOUND);
            }

            return user;
        } catch (Auth0Exception e) {
            throw new CustomException(ErrorCode.AUTH0_API_ERROR);
        }
    }
}