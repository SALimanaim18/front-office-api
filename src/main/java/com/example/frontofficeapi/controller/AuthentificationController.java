package com.example.frontofficeapi.controller;


import com.example.frontofficeapi.auth.AuthenticationRequest;
import com.example.frontofficeapi.auth.AuthentificationResponse;
import com.example.frontofficeapi.auth.RegistreRequest;
import com.example.frontofficeapi.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/SangConnect/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor

public class AuthentificationController {

    private final AuthenticationService service;

    @PostMapping("/register")

    public ResponseEntity<AuthentificationResponse> register(
            @RequestBody RegistreRequest request
    ) {

        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthentificationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }
}
