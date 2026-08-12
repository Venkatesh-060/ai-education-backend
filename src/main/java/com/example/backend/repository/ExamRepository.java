package com.example.backend.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.backend.model.Exam;

public interface ExamRepository extends MongoRepository<Exam, String> {

    List<Exam> findByTrainerId(String trainerId);
    List<Exam> findByStatus(String status);
    List<Exam> findByBatchId(String batchId);
    List<Exam> findByCourseId(String courseId);
}