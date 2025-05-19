package com.example.frontofficeapi.auth;



import com.example.frontofficeapi.entity.City;
import lombok.*;

import java.util.Date;


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegistreRequest {
    private String firstName;
    private String lastName;
    private String email;
    private Date birthDate;
    private String phone;
    private String city;
    private Long centerId;
    private String password;
}
