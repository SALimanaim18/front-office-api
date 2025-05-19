package com.example.frontofficeapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@Table(name = "REQUESTS")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Blood type is required")
    @Column(name = "blood_type", nullable = false)
    private String bloodType;

    @ManyToOne(optional = false)
    @JoinColumn(name = "city_id", nullable = false)
    private City city;

    @Enumerated(EnumType.STRING)
    @Column(name = "urgency_level", nullable = false)
    private UrgencyLevel urgencyLevel;

    @Size(max = 1000)
    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "donation_center_id", nullable = false)
    private DonationCenter donationCenter;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "required_blood_units", nullable = false)
    private Integer requiredBloodUnits;


    @Column(name = "confirmed_by_center_manager", nullable = false)
    private boolean confirmedByCenterManager = false;

}
