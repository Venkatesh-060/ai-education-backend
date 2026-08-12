package com.example.backend.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.backend.model.ExamResult;

public interface ExamResultRepository
        extends MongoRepository<ExamResult, String> {
    List<ExamResult> findByExamId(String examId);
    List<ExamResult> findByStudentId(String studentId);
    long countByExamId(String examId);
}