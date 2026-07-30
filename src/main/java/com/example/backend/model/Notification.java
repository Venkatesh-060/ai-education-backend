package com.example.backend.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "notifications")
public class Notification {

    @Id
    private String id;
    private String title;
    private String message;
    private String senderId;
    private String senderRole;
    private String recipientType;
    private String recipientId;
    private String batchId;
    private String priority;
    private Boolean readStatus = false;
    private String status = "ACTIVE";
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String sessionId;

}