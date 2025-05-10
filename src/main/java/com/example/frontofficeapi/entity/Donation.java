package com.example.frontofficeapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@Table(name = "DONATIONS")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class Donation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "donation_seq")
    @SequenceGenerator(name = "donation_seq", sequenceName = "DONATION_SEQ", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "request_id")
    private Request request;  // Optional

    @ManyToOne
    @JoinColumn(name = "center_id", nullable = false)
    private DonationCenter donationCenter;

    @Column(name = "donation_date", nullable = false)
    private LocalDateTime date;

    @Column(name = "blood_type", nullable = false, length = 10)
    private String bloodType;

    @Column(name = "volume_ml", nullable = false)
    private Integer volumeMl;

    @Column(nullable = false)
    private boolean validated;
}
