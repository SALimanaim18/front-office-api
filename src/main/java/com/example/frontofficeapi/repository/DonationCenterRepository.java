package com.example.frontofficeapi.repository;

import com.example.frontofficeapi.entity.City;
import com.example.frontofficeapi.entity.DonationCenter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DonationCenterRepository extends JpaRepository<DonationCenter, Long> {

    List<DonationCenter> findByCity(City city);

    List<DonationCenter> findByType(String type);

    List<DonationCenter> findByCityAndType(City city, String type);

    List<DonationCenter> findByNameContainingIgnoreCase(String name);

    List<DonationCenter> findByCity_NameIgnoreCase(String name);

    boolean existsByName(String name);
}
