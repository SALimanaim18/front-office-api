package com.example.frontofficeapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "CITIES")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "city")
    private List<DonationCenter> donationCenters;

    @JsonIgnore
    @OneToMany(mappedBy = "city")
    private List<Request> requests;
}
