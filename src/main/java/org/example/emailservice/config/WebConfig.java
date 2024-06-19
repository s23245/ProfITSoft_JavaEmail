package org.example.emailservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.Properties;

@Configuration
public class WebConfig {

    @Value("${SPRING_MAIL_HOST}")
    private String mailHost;

    @Value("${SPRING_MAIL_PORT}")
    private int mailPort;

    @Value("${SPRING_MAIL_USERNAME}")
    private String mailUsername;

    @Value("${SPRING_MAIL_PASSWORD}")
    private String mailPassword;

    @Value("${SPRING_RABBITMQ_HOST}")
    private String rabbitmqHost;

    @Value("${SPRING_RABBITMQ_PORT}")
    private int rabbitmqPort;

    @Value("${SPRING_RABBITMQ_USERNAME}")
    private String rabbitmqUsername;

    @Value("${SPRING_RABBITMQ_PASSWORD}")
    private String rabbitmqPassword;

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailHost);
        mailSender.setPort(mailPort);

        mailSender.setUsername(mailUsername);
        mailSender.setPassword(mailPassword);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }

    @Bean
    public Queue emailQueue() {
        return new Queue("emailQueue", false);
    }

    @Bean
    public CachingConnectionFactory rabbitConnectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(rabbitmqHost);
        connectionFactory.setUsername(rabbitmqUsername);
        connectionFactory.setPassword(rabbitmqPassword);
        return connectionFactory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(CachingConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }
}
