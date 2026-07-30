package com.example.backend.repository;

import com.example.backend.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.List;

public interface NotificationRepository
                extends MongoRepository<Notification, String> {

        List<Notification> findByRecipientId(String recipientId);

        List<Notification> findByPriority(String priority);

        List<Notification> findByStatus(String status);

        List<Notification> findByRecipientType(String recipientType);

        List<Notification> findByTitleContainingIgnoreCase(String keyword);

        @Query("""
                        {
                          'status':'ACTIVE',
                          '$or':[
                              { 'recipientType':'ALL' },

                              {
                                'recipientType':'USER',
                                'recipientId': ?0
                              },

                              {
                                'recipientType':'BATCH',
                                'batchId': ?1
                              },

                              {
                                'recipientType':'LIVECLASSROOM',
                                'sessionId': ?2
                              }
                          ]
                        }
                        """)
        List<Notification> findAllForUser(
                        String userId,
                        String batchId,
                        String sessionId);

        Page<Notification> findByStatusOrderByCreatedAtDesc(
                        String status,
                        Pageable pageable);

        Page<Notification> findByRecipientIdAndReadStatus(
                        String userId,
                        Boolean readStatus,
                        Pageable pageable);
}