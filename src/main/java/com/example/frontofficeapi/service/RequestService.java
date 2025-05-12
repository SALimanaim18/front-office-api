//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.frontofficeapi.service;

import com.example.frontofficeapi.entity.Request;
import com.example.frontofficeapi.entity.User;
import com.example.frontofficeapi.repository.RequestRepository;
import java.util.List;
import java.util.Optional;

import com.example.frontofficeapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class RequestService {
    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private NotificationService notificationService;

    public RequestService() {
    }

    public Request saveDemande(Request request) {
        Request savedRequest = requestRepository.save(request);

        String cityName = savedRequest.getCity().getName();
        Long demandeurId = savedRequest.getUser().getId();

        List<User> users = userRepository.findByCityIgnoreCaseAndIdNot(cityName, demandeurId);

        String subject = "🩸 Nouvelle demande de sang à " + cityName;
        String message = "Demande urgente à " + cityName +
                " pour le groupe " + savedRequest.getBloodType() +
                ". Centre : " + savedRequest.getDonationCenter().getName();

        for (User user : users) {
            notificationService.sendNotificationToUser(user.getId(), message);
            emailService.sendEmail(user.getEmail(), subject, message);
        }

        return savedRequest;
    }


    public List<Request> getAll() {
        return requestRepository.findAll();
    }


    public Optional<Request> getById(Long id) {
        return this.requestRepository.findById(id);
    }

    public void deleteById(Long id) {
        this.requestRepository.deleteById(id);
    }
}
