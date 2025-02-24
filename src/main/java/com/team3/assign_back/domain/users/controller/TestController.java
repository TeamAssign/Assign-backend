package com.team3.assign_back.domain.users.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class TestController {

    private String extractUserId(String sub) {
        return sub != null && sub.startsWith("auth0|") ? sub.substring(6) : sub;
    }

    @GetMapping("/hello")
    public ResponseEntity<Map<String, String>> getHello(@AuthenticationPrincipal Jwt jwt) {
        String userId = extractUserId(jwt.getClaimAsString("sub"));
        return ResponseEntity.ok(Map.of("message", "Hello, User " + userId + "!"));
    }

    @PostMapping("/message")
    public ResponseEntity<Map<String, String>> postMessage(@AuthenticationPrincipal Jwt jwt,
                                                           @RequestBody Map<String, String> request) {
        String userId = extractUserId(jwt.getClaimAsString("sub"));
        String message = request.get("message");
        return ResponseEntity.ok(Map.of("message", "Received from User " + userId + ": " + message));
    }
}