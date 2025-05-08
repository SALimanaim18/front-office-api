package com.example.frontofficeapi.entity;

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
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "city")
    private List<User> users;

    @OneToMany(mappedBy = "city")
    private List<DonationCenter> donationCenters;

    @OneToMany(mappedBy = "city")
    private List<Request> requests;
}
