package com.example.backend.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.backend.model.Session;

public interface SessionRepo extends MongoRepository<Session, String> {

    List<Session> findByTrainerId(String trainerId);
}