package com.example.backend.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.backend.model.Feedback;

public interface FeedbackRepository extends MongoRepository<Feedback, String> {

    boolean existsBySessionIdAndStudentId(String sessionId, String studentId);

    List<Feedback> findBySessionId(String sessionId);

    List<Feedback> findByTrainerId(String trainerId);

    List<Feedback> findByStudentId(String studentId);

    List<Feedback> findByRating(Integer rating);

    Page<Feedback> findAll(Pageable pageable);

    Page<Feedback> findByReviewContainingIgnoreCase(String review, Pageable pageable);
}