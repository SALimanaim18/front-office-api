package com.example.frontofficeapi.auth;


import lombok.*;



@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AuthenticationRequest {
    String email;
    String password;
}
