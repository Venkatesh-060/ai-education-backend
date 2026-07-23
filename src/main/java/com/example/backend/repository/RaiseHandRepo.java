package com.example.backend.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.backend.model.RaiseHand;

public interface RaiseHandRepo extends MongoRepository<RaiseHand, String> {

    List<RaiseHand> findBySessionId(String sessionId);

    List<RaiseHand> findByStudentIdAndSessionId(
            String studentId,
            String sessionId);

    List<RaiseHand> findBySessionIdAndStatus(
            String sessionId,
            String status);
}