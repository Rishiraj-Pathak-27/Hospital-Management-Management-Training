package com.hospital.auth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthResponse {

    private String message;
    private String token;
    private String username;
    private String role;
    private Long expiresIn;
    private Boolean valid;
    private String error;

    public static AuthResponse registerSuccess(String username, String role) {
        return AuthResponse.builder()
                .message("User registered successfully")
                .username(username)
                .role(role)
                .build();
    }

    public static AuthResponse loginSuccess(String token, String username, String role, Long expiresIn) {
        return AuthResponse.builder()
                .token(token)
                .username(username)
                .role(role)
                .expiresIn(expiresIn)
                .build();
    }

    public static AuthResponse validateSuccess(String username, String role) {
        return AuthResponse.builder()
                .valid(true)
                .username(username)
                .role(role)
                .build();
    }

    public static AuthResponse validateFailure(String message) {
        return AuthResponse.builder()
                .valid(false)
                .message(message)
                .build();
    }

}
