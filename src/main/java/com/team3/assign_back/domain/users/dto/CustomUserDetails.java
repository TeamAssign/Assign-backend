package com.team3.assign_back.domain.users.dto;

import com.auth0.json.mgmt.users.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {
    private final User auth0User;

    @Override
    public String getUsername() {
        return auth0User.getId();
    }

    public String getEmail() {
        return auth0User.getEmail();
    }

    public String getName() {
        return auth0User.getName();
    }

    public String getPicture() {
        return auth0User.getPicture();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}