package com.example.backend.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.backend.model.Exam;
import com.example.backend.repository.ExamRepository;

@RestController
@RequestMapping("/api/student/exams")
@CrossOrigin(origins = "http://localhost:5173")
public class StudentExamController {

    private final ExamRepository examRepository;

    public StudentExamController(
            ExamRepository examRepository) {

        this.examRepository = examRepository;
    }

    @GetMapping("/published")
    public ResponseEntity<List<Exam>> getPublishedExams() {

        return ResponseEntity.ok(
                examRepository.findByStatus("Published"));
    }
}