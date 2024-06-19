package org.example.emailservice;

import org.example.emailservice.ElasticsearchTestConfiguration;
import org.example.emailservice.model.Email;
import org.example.emailservice.repository.EmailRepository;
import org.example.emailservice.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ContextConfiguration;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest
@ContextConfiguration(classes = {ProfItSoftJavaEmailApplication.class, ElasticsearchTestConfiguration.class})
class ProfItSoftJavaEmailApplicationTests {

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private EmailRepository emailRepository;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendEmail_Success() {
        // Arrange
        Email email = new Email();
        email.setRecipient("test@example.com");
        email.setSubject("Test Subject");
        email.setContent("Test Content");

        // Act
        emailService.sendEmail(email);

        // Assert
        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
        verify(emailRepository, times(1)).save(email);
        assert "sent".equals(email.getStatus());
        assert email.getErrorMessage() == null;
    }

    @Test
    void testSendEmail_Error() {
        // Arrange
        Email email = new Email();
        email.setRecipient("test@example.com");
        email.setSubject("Test Subject");
        email.setContent("Test Content");

        doThrow(new RuntimeException("Mail server error")).when(javaMailSender).send(any(SimpleMailMessage.class));

        // Act
        emailService.sendEmail(email);

        // Assert
        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
        verify(emailRepository, times(1)).save(email);
        assert "erroneous".equals(email.getStatus());
        assert email.getErrorMessage() != null;
        assert email.getAttemptCount() == 1;
    }

    @Test
    void testRetryFailedEmails() {
        // Arrange
        Email failedEmail = new Email();
        failedEmail.setRecipient("test@example.com");
        failedEmail.setSubject("Test Subject");
        failedEmail.setContent("Test Content");
        failedEmail.setStatus("erroneous");

        List<Email> failedEmails = Collections.singletonList(failedEmail);
        when(emailRepository.findByStatus("erroneous")).thenReturn(failedEmails);

        // Act
        emailService.retryFailedEmails();

        // Assert
        verify(emailRepository, times(1)).findByStatus("erroneous");
        verify(emailRepository, times(1)).save(failedEmail);
        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}
