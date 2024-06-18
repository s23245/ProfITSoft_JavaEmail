package org.example.emailservice.config;

import org.example.emailservice.model.Email;
import org.example.emailservice.service.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageListener
{
    @Autowired
    private EmailService emailService;

    @RabbitListener(queues = "email-queue")
    public void receiveMessage(Email email) {
        emailService.sendEmail(email);
    }
}
