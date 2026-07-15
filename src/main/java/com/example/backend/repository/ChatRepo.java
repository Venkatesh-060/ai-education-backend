package com.example.backend.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.backend.model.Chat;

public interface ChatRepo extends MongoRepository<Chat, String> {

    List<Chat> findBySessionIdOrderByTimestampAsc(String sessionId);

}