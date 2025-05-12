package com.example.frontofficeapi.repository;

import com.example.frontofficeapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findByCityIgnoreCaseAndIdNot(String cityName, Long excludedUserId);
}
