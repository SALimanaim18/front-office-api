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
@EqualsAndHashCode(of = "id")
@Table(name = "DONATION_CENTERS")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class DonationCenter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(optional = false)
    @JoinColumn(name = "city_id", nullable = false)
    private City city;

    @Column(nullable = false)
    private String address;

    @Column(name = "contact_phone", nullable = false)
    private String contactPhone;

    @Column(nullable = false)
    private String type;

    @OneToMany(mappedBy = "donationCenter", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Request> requests;

    @OneToMany(mappedBy = "donationCenter", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "donationCenter")
    @JsonIgnore
    private List<User> managers;
}
