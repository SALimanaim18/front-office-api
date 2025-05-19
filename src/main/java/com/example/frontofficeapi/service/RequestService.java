package com.example.frontofficeapi.service;

import com.example.frontofficeapi.dto.RequestDto;
import com.example.frontofficeapi.entity.Request;
import com.example.frontofficeapi.entity.UrgencyLevel;
import com.example.frontofficeapi.entity.User;
import com.example.frontofficeapi.repository.RequestRepository;
import com.example.frontofficeapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    public Request saveDemande(Request request) {
        // Enregistrer la demande dans la base de données
        Request savedRequest = requestRepository.save(request);

        // Récupérer les informations nécessaires
        String cityName = savedRequest.getCity().getName();
        Long demandeurId = savedRequest.getUser().getId();
        String bloodType = savedRequest.getBloodType();
        String centerName = savedRequest.getDonationCenter().getName();

        // Trouver les utilisateurs de la même ville, à l'exception du demandeur
        List<User> eligibleDonors = userRepository.findByCityIgnoreCaseAndIdNot(cityName, demandeurId);

        // Sujet de l'email
        String subject = "🩸 Urgence don de sang : Groupe " + bloodType + " nécessaire à " + cityName;

        // Pour chaque utilisateur éligible, envoyer une notification et un email personnalisé
        for (User donor : eligibleDonors) {
            // Envoi de la notification in-app
            notificationService.sendNotificationToUser(donor.getId(),
                    "Demande urgente de sang groupe " + bloodType + " à " + cityName);

            // Création d'un email professionnel et personnalisé
            String emailContent = createProfessionalEmailContent(
                    donor.getFirstName(),
                    bloodType,
                    cityName,
                    centerName,
                    savedRequest.getId()
            );

            // Envoi de l'email avec contenu HTML
            emailService.sendHtmlEmail(donor.getEmail(), subject, emailContent);
        }

        return savedRequest;
    }

    /**
     * Crée un contenu d'email professionnel et personnalisé en HTML
     *
     * @param donorName Prénom du donneur potentiel
     * @param bloodType Type de sang demandé
     * @param cityName Nom de la ville
     * @param centerName Nom du centre de don
     * @param requestId Identifiant de la demande
     * @return Contenu HTML de l'email
     */
    private String createProfessionalEmailContent(String donorName, String bloodType, String cityName,
                                                  String centerName, Long requestId) {
        // Lien vers la plateforme avec l'ID de la demande
        String actionUrl = baseUrl + "/donation/respond?requestId=" + requestId;

        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <style>
                    body { font-family: 'Segoe UI', Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background-color: #b22222; color: white; padding: 20px; text-align: center; border-radius: 5px 5px 0 0; }
                    .content { background-color: #f9f9f9; padding: 20px; border-left: 1px solid #ddd; border-right: 1px solid #ddd; }
                    .footer { background-color: #f1f1f1; padding: 15px; text-align: center; font-size: 12px; color: #777; border-radius: 0 0 5px 5px; }
                    .btn { display: inline-block; background-color: #b22222; color: white; padding: 12px 24px; text-decoration: none; border-radius: 4px; font-weight: bold; margin: 20px 0; }
                    .btn:hover { background-color: #8b0000; }
                    .emphasis { color: #b22222; font-weight: bold; }
                    .info-box { background-color: #fff; border-left: 4px solid #b22222; padding: 15px; margin: 15px 0; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>SangConnect</h1>
                        <p>Ensemble, sauvons des vies</p>
                    </div>
                    
                    <div class="content">
                        <h2>Bonjour %s,</h2>
                        
                        <p>Nous vous contactons car une <span class="emphasis">demande urgente de don de sang</span> vient d'être enregistrée dans votre ville.</p>
                        
                        <div class="info-box">
                            <p><strong>Type de sang recherché :</strong> <span class="emphasis">%s</span></p>
                            <p><strong>Ville :</strong> %s</p>
                            <p><strong>Centre de don :</strong> %s</p>
                        </div>
                        
                        <p>Votre aide pourrait sauver des vies. Si vous êtes disponible pour effectuer un don, cliquez sur le bouton ci-dessous pour accéder directement à la plateforme et prendre rendez-vous.</p>
                        
                        <div style="text-align: center;">
                            <a href="%s" class="btn">Je souhaite aider</a>
                        </div>
                        
                        <p>Chaque don compte. Merci d'avance pour votre générosité.</p>
                    </div>
                    
                    <div class="footer">
                        <p>© 2025 SangConnect - Tous droits réservés</p>
                        <p>Si vous ne souhaitez plus recevoir ces notifications, <a href="%s/preferences">modifiez vos préférences</a>.</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(donorName, bloodType, cityName, centerName, actionUrl, baseUrl);
    }

    public List<RequestDto> getAll() {
        List<Request> requests = requestRepository.findAll();
        return requests.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // ✅ Méthode pour mapper une entité Request vers un DTO enrichi
    private RequestDto toDto(Request request) {
        User user = request.getUser();

        return RequestDto.builder()
                .id(request.getId())
                .bloodType(request.getBloodType())
                .donationCenter(request.getDonationCenter().getId())
                .cityId(request.getCity().getId())
                .urgencyLevel(request.getUrgencyLevel().name())
                .description(request.getDescription())
                .requiredBloodUnits(request.getRequiredBloodUnits())
                .createdAt(request.getCreatedAt())
                .confirmedByCenterManager(request.isConfirmedByCenterManager())
                .userName(user.getFirstName() + " " + user.getLastName())
                .userPhone(user.getPhone())
                .build();
    }

    public Optional<Request> getById(Long id) {
        return requestRepository.findById(id);
    }

    public void deleteById(Long id) {
        requestRepository.deleteById(id);
    }

    public Request confirmRequestByCenterManager(Long requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Demande introuvable avec ID: " + requestId));

        request.setConfirmedByCenterManager(true);
        return requestRepository.save(request);
    }

    public long countUrgentRequestsByCenter(Long centerId) {
        return requestRepository.countByDonationCenterIdAndUrgencyLevelGreaterThanEqual(centerId, UrgencyLevel.HAUTE);
    }

    public Long countRequestsByCenter(Long centerId) {
        return requestRepository.countByDonationCenterId(centerId);
    }

    public List<RequestDto> getLatestRequestsByCenter(Long centerId) {
        List<Request> requests = requestRepository.findTop4ByDonationCenterIdOrderByCreatedAtDesc(centerId);
        return requests.stream()
                .map(this::toDto) // ou un mapper personnalisé si tu en utilises un
                .collect(Collectors.toList());
    }


}