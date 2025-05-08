package com.example.frontofficeapi.repository;

import com.example.frontofficeapi.entity.Donation;
import com.example.frontofficeapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DonationRepository extends JpaRepository<Donation, Long> {

    List<Donation> findByUser(User user);

    List<Donation> findByBloodType(String bloodType);

    List<Donation> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<Donation> findByValidated(boolean validated);

    @Query("SELECT d FROM Donation d WHERE d.user.id = :userId")
    List<Donation> findByUserId(Long userId);

    @Query("SELECT COUNT(d) FROM Donation d WHERE d.user.id = :userId AND d.date >= :startDate")
    Integer countUserDonationsSince(Long userId, LocalDateTime startDate);
}