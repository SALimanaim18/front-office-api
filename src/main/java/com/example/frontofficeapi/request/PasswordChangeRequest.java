package com.example.frontofficeapi.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordChangeRequest {
    private String oldPassword;
    private String newPassword;
}
