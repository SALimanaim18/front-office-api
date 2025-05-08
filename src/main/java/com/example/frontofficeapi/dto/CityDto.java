package com.example.frontofficeapi.dto;

import lombok.AllArgsConstructor;
import lombok.*;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CityDto implements Serializable {
    private Long id;
    private String name;
}
