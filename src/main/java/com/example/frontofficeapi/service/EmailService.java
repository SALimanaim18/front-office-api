
package com.example.frontofficeapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;


@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    /**
     * Envoie un email en texte simple
     *
     * @param to Adresse email du destinataire
     * @param subject Sujet de l'email
     * @param text Contenu de l'email en texte brut
     */
    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    /**
     * Envoie un email avec contenu HTML
     *
     * @param to Adresse email du destinataire
     * @param subject Sujet de l'email
     * @param htmlContent Contenu HTML de l'email
     */
    public void sendHtmlEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // true indique que le contenu est au format HTML

            mailSender.send(message);
        } catch (MessagingException e) {
            // Loguer l'erreur et renvoyer éventuellement une exception métier
            throw new RuntimeException("Erreur lors de l'envoi de l'email HTML: " + e.getMessage(), e);
        }
    }
}