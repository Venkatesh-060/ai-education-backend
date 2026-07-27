package com.example.backend.service;

import com.example.backend.model.Notification;
import com.example.backend.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository repository;

    public Notification create(Notification notification) {

        notification.setCreatedAt(LocalDateTime.now());
        notification.setUpdatedAt(LocalDateTime.now());
        notification.setReadStatus(false);
        notification.setStatus("ACTIVE");

        return repository.save(notification);
    }

    public List<Notification> getAll() {
        return repository.findByStatus("ACTIVE");
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

    public List<Notification> getMyNotifications(String userId) {

        return repository.findByRecipientId(userId);
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