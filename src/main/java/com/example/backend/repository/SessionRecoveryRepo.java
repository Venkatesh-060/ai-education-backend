package com.example.backend.repository;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.backend.model.SessionRecovery;

public interface SessionRecoveryRepo
        extends MongoRepository<SessionRecovery, String> {

Optional<SessionRecovery> findBySessionIdAndTrainerId(
        String sessionId,
        String trainerId);
        
}