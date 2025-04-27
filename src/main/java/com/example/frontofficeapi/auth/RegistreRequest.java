package com.example.frontofficeapi.auth;



import lombok.*;


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegistreRequest {
    private String name;
    private String email;
    private String password;
    private String phone;
}
