package org.example.emailservice.service;

import org.example.emailservice.model.Email;
import org.example.emailservice.repository.EmailRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@EnableScheduling
public class EmailService {

    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private JavaMailSender mailSender;

    @RabbitListener(queues = "email-queue")
    public void receiveEmail(Email email) {
        email.setStatus("PENDING");
        emailRepository.save(email);
        sendEmail(email);
    }

    public void sendEmail(Email email) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email.getRecipient());
            message.setSubject(email.getSubject());
            message.setText(email.getContent());
            mailSender.send(message);
            email.setStatus("SENT");
        } catch (Exception e) {
            email.setStatus("ERROR");
            email.setErrorMessage(e.getMessage());
        }
        emailRepository.save(email);
    }

    @Scheduled(fixedRate = 300000)
    public void resendFailedEmails() {
        List<Email> failedEmails = emailRepository.findByStatus("ERROR");
        for (Email email : failedEmails) {
            sendEmail(email);
        }
    }
}
