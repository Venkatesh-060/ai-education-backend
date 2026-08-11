package com.example.backend.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.backend.model.Participant;

public interface ParticipantRepo extends MongoRepository<Participant, String> {

    List<Participant> findBySessionId(String sessionId);

    List<Participant> findBySessionIdAndStatus(
            String sessionId,
            String status);

    List<Participant> findBySessionIdAndUserId(
            String sessionId,
            String userId);

    Optional<Participant> findFirstBySessionIdAndUserId(
            String sessionId,
            String userId);

    long countBySessionId(String sessionId);

    long countBySessionIdAndStatus(
            String sessionId,
            String status);
}