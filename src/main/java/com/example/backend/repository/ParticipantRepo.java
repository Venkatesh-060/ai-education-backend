package com.example.backend.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.backend.model.Participant;

public interface ParticipantRepo extends MongoRepository<Participant, String> {

    List<Participant> findBySessionId(String sessionId);

     List<Participant> findBySessionIdAndUserId(
            String sessionId,
            String userId);

}