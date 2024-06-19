package org.example.emailservice.service;

import org.example.emailservice.model.Email;
import org.example.emailservice.repository.EmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private EmailRepository emailRepository;

    public void sendEmail(Email email) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email.getRecipient());
            message.setSubject(email.getSubject());
            message.setText(email.getContent());
            javaMailSender.send(message);

            email.setStatus("sent");
            email.setErrorMessage(null);
        } catch (Exception e) {
            email.setStatus("erroneous");
            email.setErrorMessage(e.getClass().getName() + ": " + e.getMessage());
            email.setAttemptCount(email.getAttemptCount() + 1);
            email.setLastAttempt(System.currentTimeMillis());
        }
        //emailRepository.save(email);
    }

    @Scheduled(fixedRate = 300000) // 5 minutes
    public void retryFailedEmails() {
        List<Email> failedEmails = emailRepository.findByStatus("erroneous");
        for (Email email : failedEmails) {
            sendEmail(email);
        }
    }
}
