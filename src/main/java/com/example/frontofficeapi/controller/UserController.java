package com.example.frontofficeapi.controller;

import com.example.frontofficeapi.entity.User;
import com.example.frontofficeapi.request.PasswordChangeRequest;
import com.example.frontofficeapi.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
@RequestMapping("/SangConnect/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<User> getProfile(Principal principal) {
        return ResponseEntity.ok(userService.getProfile(principal.getName()));
    }

    @PutMapping("/me")
    public ResponseEntity<User> updateProfile(@RequestBody User updatedUser, Principal principal) {
        return ResponseEntity.ok(userService.updateProfile(principal.getName(), updatedUser));
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody PasswordChangeRequest request, Principal principal) {
        return userService.changePassword(principal.getName(), request);
    }

    @PostMapping("/upload-photo")
    public ResponseEntity<?> uploadPhoto(@RequestParam("file") MultipartFile file, Principal principal) {
        return userService.uploadPhoto(file, principal.getName());
    }
}
