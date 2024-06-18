package org.example.emailservice.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.time.LocalDateTime;

@Data
@Document(indexName = "emails")
public class Email
{
    @Id
    private String id;
    private String subject;
    private String content;
    private String recipient;
    private String status;
    private String errorMessage;
    private int attemptCount;
    private LocalDateTime lastAttempt;
}
