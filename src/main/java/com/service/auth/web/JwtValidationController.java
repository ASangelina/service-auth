package com.service.auth.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class JwtValidationController {

    private final JwtDecoder jwtDecoder;

    public JwtValidationController(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    @GetMapping("validate")
    public ResponseEntity<Boolean> validateToken(@RequestHeader("Authorization") String authorizationHeader) {

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
        }

        String token = authorizationHeader.substring(7); // Remove "Bearer " prefix

        try {
            jwtDecoder.decode(token);
            return ResponseEntity.ok(true);
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
        }
    }
}

