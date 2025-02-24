package com.team3.assign_back.domain.users.controller;

import com.team3.assign_back.domain.users.dto.CustomUserDetails;
import com.team3.assign_back.domain.users.dto.UserRegisterRequestDto;
import com.team3.assign_back.domain.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody UserRegisterRequestDto userRegisterRequestDto) {
        String vendorId = userDetails.getUsername();
        userService.registerUser(vendorId, userRegisterRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}