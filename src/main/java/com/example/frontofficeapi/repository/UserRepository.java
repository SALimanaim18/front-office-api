package com.example.frontofficeapi.repository;

import com.example.frontofficeapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findByCityIgnoreCaseAndIdNot(String cityName, Long excludedUserId);
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.donationCenter WHERE u.email = :email")
    Optional<User> findByEmailWithCenter(@Param("email") String email);

}
