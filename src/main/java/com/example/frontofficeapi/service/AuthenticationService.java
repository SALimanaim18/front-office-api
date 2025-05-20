package com.example.frontofficeapi.service;

import com.example.frontofficeapi.auth.AuthenticationRequest;
import com.example.frontofficeapi.auth.AuthentificationResponse;
import com.example.frontofficeapi.auth.RegistreRequest;
import com.example.frontofficeapi.entity.DonationCenter;
import com.example.frontofficeapi.entity.Role;
import com.example.frontofficeapi.entity.User;
import com.example.frontofficeapi.repository.DonationCenterRepository;
import com.example.frontofficeapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final DonationCenterRepository donationCenterRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthentificationResponse register(RegistreRequest request) {
        User.UserBuilder userBuilder = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .birthDate(request.getBirthDate())
                .city(request.getCity())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()));

        if (request.getCenterId() != null) {
            DonationCenter center = donationCenterRepository.findById(request.getCenterId())
                    .orElseThrow(() -> new RuntimeException("Donation center not found"));
            userBuilder
                    .donationCenter(center)
                    .role(Role.CENTER_MANAGER);
        } else {
            userBuilder.role(Role.USER);
        }

        User user = userBuilder.build();
        repository.save(user);

        String jwtToken = jwtService.generateToken(user);

        return AuthentificationResponse.builder()
                .token(jwtToken)
                .userId(user.getId())
                .role(user.getRole().name())
                .centerId(user.getDonationCenter() != null ? user.getDonationCenter().getId() : null)
                .build();
    }

    public AuthentificationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));

        String jwtToken = jwtService.generateToken(user);

        return AuthentificationResponse.builder()
                .token(jwtToken)
                .userId(user.getId())
                .role(user.getRole().name())
                .centerId(user.getDonationCenter() != null ? user.getDonationCenter().getId() : null)
                .build();
    }
}