package com.example.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NotificationRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Message is required")
    private String message;

    @NotBlank(message = "Sender Id is required")
    private String senderId;

    @NotBlank(message = "Sender Role is required")
    private String senderRole;

    @NotBlank(message = "Recipient Type is required")
    private String recipientType;

    private String batchId;

    private String sessionId;
    private String recipientId;
    @NotBlank(message = "Priority is required")
    private String priority;

}