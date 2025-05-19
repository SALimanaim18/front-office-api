package com.example.frontofficeapi.service;

import com.example.frontofficeapi.entity.User;
import com.example.frontofficeapi.repository.UserRepository;
import com.example.frontofficeapi.request.PasswordChangeRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User getProfile(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
    }

    @Transactional
    public User updateProfile(String email, User updatedData) {
        User user = getProfile(email);

        user.setFirstName(updatedData.getFirstName());
        user.setLastName(updatedData.getLastName());
        user.setPhone(updatedData.getPhone());
        user.setCity(updatedData.getCity());
        user.setEmail(updatedData.getEmail());

        return userRepository.save(user);
    }

    @Transactional
    public ResponseEntity<String> changePassword(String email, PasswordChangeRequest request) {
        User user = getProfile(email);

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body("Ancien mot de passe incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        return ResponseEntity.ok("Mot de passe mis à jour");
    }

    @Transactional
    public ResponseEntity<String> uploadPhoto(MultipartFile file, String email) {
        User user = getProfile(email);

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path uploadDir = Paths.get("uploads");
        Path filePath = uploadDir.resolve(fileName);

        try {
            Files.createDirectories(uploadDir);

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            user.setPhotoUrl("/uploads/" + fileName);
            userRepository.save(user);

            return ResponseEntity.ok("Photo mise à jour avec succès.");
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Erreur lors de l'upload de la photo : " + e.getMessage());
        }
    }
}
