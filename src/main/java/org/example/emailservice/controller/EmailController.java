package org.example.emailservice.controller;

import org.example.emailservice.model.Email;
import org.example.emailservice.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/emails")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    public String sendEmail(@RequestBody Email email) {
        System.out.println("Received request to send email: " + email);
        emailService.sendEmail(email);
        return "Email sent successfully";
    }
}
