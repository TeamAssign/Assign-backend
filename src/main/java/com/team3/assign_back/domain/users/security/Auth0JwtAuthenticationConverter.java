package com.team3.assign_back.domain.users.security;

import com.team3.assign_back.domain.users.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import com.auth0.json.mgmt.users.User;

@Component
@RequiredArgsConstructor
public class Auth0JwtAuthenticationConverter implements Converter<Jwt, JwtAuthenticationToken> {

    @Override
    public JwtAuthenticationToken convert(Jwt jwt) {
        String vendorId = jwt.getSubject();

        CustomUserDetails userDetails = new CustomUserDetails(vendorId);
        return new JwtAuthenticationToken(jwt, userDetails.getAuthorities(), userDetails.getUsername());
    }
}