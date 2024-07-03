package com.service.auth.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Valida um token JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token JWT válido",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "true"))),
            @ApiResponse(responseCode = "401", description = "Token JWT inválido ou ausente",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "false")))
    })
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

