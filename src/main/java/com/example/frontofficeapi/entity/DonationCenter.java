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
@EqualsAndHashCode(of = "id")
@Table(name = "DONATION_CENTERS")
public class DonationCenter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String address;

    @Column(name = "contact_phone", nullable = false)
    private String contactPhone;

    @Column(nullable = false)
    private String type;

    @OneToMany(mappedBy = "donationCenter", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Request> requests;
}
