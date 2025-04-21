package com.example.frontofficeapi.service;


import com.example.frontofficeapi.auth.AuthenticationRequest;
import com.example.frontofficeapi.auth.AuthentificationResponse;
import com.example.frontofficeapi.auth.RegistreRequest;
import com.example.frontofficeapi.entity.Role;
import com.example.frontofficeapi.entity.User;
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
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    public AuthentificationResponse register(RegistreRequest request) {
        var user= User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return new AuthentificationResponse()
                .builder()
                .token(jwtToken)
                .build();
    }


    public AuthentificationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();

        var jwtToken = jwtService.generateToken(user);
        return new AuthentificationResponse()
                .builder()
                .token(jwtToken)
                .build();
    }

    }

