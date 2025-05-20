package com.example.frontofficeapi.repository;

import com.example.frontofficeapi.entity.Request;
import com.example.frontofficeapi.entity.UrgencyLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    @Query("SELECT COUNT(r) FROM Request r WHERE r.donationCenter.id = :centerId")
    Long countByDonationCenterId(@Param("centerId") Long centerId);

    long countByDonationCenterIdAndUrgencyLevelGreaterThanEqual(Long centerId, UrgencyLevel urgencyLevel);
    List<Request> findByDonationCenterId(Long centerId);

    List<Request> findTop4ByDonationCenterIdOrderByCreatedAtDesc(Long centerId);

    List<Request> findByUserId(Long userId);
}