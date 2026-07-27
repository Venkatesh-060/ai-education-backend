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

    // ALL / BATCH / USER
    private String recipientType;

    private String recipientId;

    private String batchId;

    // LOW / MEDIUM / HIGH
    private String priority;

    private Boolean readStatus = false;

    // ACTIVE / DELETED
    private String status = "ACTIVE";

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}