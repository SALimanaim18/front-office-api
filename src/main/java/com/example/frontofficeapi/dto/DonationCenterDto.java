package com.example.frontofficeapi.dto;

import com.example.frontofficeapi.entity.City;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DonationCenterDto implements Serializable {

    private Long id;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "City is required")
    @Size(min = 2, max = 50, message = "City must be between 2 and 50 characters")
    private City city;

    @NotBlank(message = "Address is required")
    @Size(min = 5, max = 200, message = "Address must be between 5 and 200 characters")
    private String address;

    @NotBlank(message = "Contact phone number is required")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Phone number must be valid format")
    private String contactPhone;

    @NotBlank(message = "Center type is required")
    @Pattern(regexp = "^(BLOOD|PLASMA|PLATELETS|MEDICAL|CRISIS|GENERAL)$",
            message = "Type must be one of: BLOOD, PLASMA, PLATELETS, MEDICAL, CRISIS, GENERAL")
    private String type;
}