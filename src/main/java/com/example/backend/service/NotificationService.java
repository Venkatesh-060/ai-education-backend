package com.example.backend.service;

import com.example.backend.dto.NotificationRequest;
import com.example.backend.model.Notification;
import com.example.backend.repository.BatchRepo;
import com.example.backend.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.backend.repository.SessionRepo;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository repository;
    private final SessionRepo sessionRepo;
    private final BatchRepo batchRepo;

    public Notification create(Notification notification) {

        if (notification.getTitle() == null ||
                notification.getTitle().trim().isEmpty()) {

            throw new RuntimeException("Title is required");
        }

        // Message Validation
        if (notification.getMessage() == null ||
                notification.getMessage().trim().isEmpty()) {

            throw new RuntimeException("Message is required");
        }

        // Priority Validation
        List<String> priorities = List.of("LOW", "MEDIUM", "HIGH", "EMERGENCY");

        if (!priorities.contains(
                notification.getPriority().toUpperCase())) {

            throw new RuntimeException("Invalid Priority");
        }

        // Recipient Type Validation
        List<String> recipients = List.of("ALL", "USER", "BATCH", "LIVECLASSROOM");

        if (!recipients.contains(
                notification.getRecipientType().toUpperCase())) {

            throw new RuntimeException("Invalid Recipient Type");
        }

        if (notification.getSessionId() != null &&
                !notification.getSessionId().isBlank()) {

            if (sessionRepo.findById(notification.getSessionId()).isEmpty()) {

                throw new RuntimeException("Session Not Found");
            }
        }

        if (notification.getBatchId() != null &&
                !notification.getBatchId().isBlank()) {

            if (!batchRepo.existsById(notification.getBatchId())) {

                throw new RuntimeException("Batch Not Found");
            }
        }

        notification.setCreatedAt(LocalDateTime.now());
        notification.setUpdatedAt(LocalDateTime.now());
        notification.setReadStatus(false);
        notification.setStatus("ACTIVE");

        return repository.save(notification);
    }

    public Notification create(NotificationRequest request) {

    Notification notification = new Notification();

    notification.setTitle(request.getTitle());
    notification.setMessage(request.getMessage());
    notification.setSenderId(request.getSenderId());
    notification.setSenderRole(request.getSenderRole());
    notification.setRecipientType(request.getRecipientType());
    notification.setRecipientId(request.getRecipientId());
    notification.setBatchId(request.getBatchId());
    notification.setSessionId(request.getSessionId());
    notification.setPriority(request.getPriority());

    return create(notification);
}

    public Page<Notification> getAll(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        return repository.findByStatusOrderByCreatedAtDesc(
                "ACTIVE",
                pageable);
    }

    public Page<Notification> unread(
            String userId,
            int page,
            int size) {

        Pageable pageable = PageRequest.of(page, size);

        return repository.findByRecipientIdAndReadStatus(
                userId,
                false,
                pageable);
    }

    public Notification get(String id) {

        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
    }

    public Notification update(String id, Notification request) {

        Notification notification = get(id);

        notification.setTitle(request.getTitle());
        notification.setMessage(request.getMessage());
        notification.setPriority(request.getPriority());
        notification.setRecipientType(request.getRecipientType());
        notification.setRecipientId(request.getRecipientId());
        notification.setBatchId(request.getBatchId());
        notification.setSessionId(request.getSessionId());
        notification.setUpdatedAt(LocalDateTime.now());

        return repository.save(notification);
    }

    public void markRead(String id) {

        Notification notification = get(id);

        notification.setReadStatus(true);
        notification.setUpdatedAt(LocalDateTime.now());

        repository.save(notification);
    }

    public void delete(String id) {

        Notification notification = get(id);

        notification.setStatus("DELETED");
        notification.setUpdatedAt(LocalDateTime.now());

        repository.save(notification);
    }

    // Logged in User Notifications

    public List<Notification> getMyNotifications(
            String userId,
            String batchId,
            String sessionId) {

        return repository.findAllForUser(
                userId,
                batchId,
                sessionId);
    }

    // Search

    public List<Notification> search(String keyword) {

        return repository.findByTitleContainingIgnoreCase(keyword);
    }

    // Priority Filter

    public List<Notification> getByPriority(String priority) {

        return repository.findByPriority(priority);
    }

    // Recipient Type Filter

    public List<Notification> getByRecipientType(String type) {

        return repository.findByRecipientType(type);
    }

    // Status Filter

    public List<Notification> getByStatus(String status) {

        return repository.findByStatus(status);
    }

}