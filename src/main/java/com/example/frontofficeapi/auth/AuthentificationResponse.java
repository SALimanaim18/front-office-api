package com.example.frontofficeapi.auth;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthentificationResponse {
    private String token;
    private String role;
    private Long userId;
    private Long centerId;
}