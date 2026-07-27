package com.example.backend.repository;

import com.example.backend.model.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface NotificationRepository extends MongoRepository<Notification, String> {

    List<Notification> findByRecipientId(String recipientId);

    List<Notification> findByPriority(String priority);

    List<Notification> findByStatus(String status);

    List<Notification> findByRecipientType(String recipientType);

    List<Notification> findByTitleContainingIgnoreCase(String keyword);
}