package org.example.emailservice.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.time.LocalDateTime;
@Document(indexName = "emails")
@Getter
@Setter
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
    private long lastAttempt;
}
