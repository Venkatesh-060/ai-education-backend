package com.example.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.backend.model.Whiteboard;

public interface WhiteboardRepo extends MongoRepository<Whiteboard, String> {

    List<Whiteboard> findBySessionId(String sessionId);

    Optional<Whiteboard> findFirstBySessionId(String sessionId);

    void deleteBySessionId(String sessionId);
}