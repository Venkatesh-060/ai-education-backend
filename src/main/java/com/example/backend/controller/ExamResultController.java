package com.example.backend.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.backend.model.ExamResult;
import com.example.backend.repository.ExamResultRepository;

@RestController
@RequestMapping("/api/exams")
@CrossOrigin(origins = "http://localhost:5173")
public class ExamResultController {

    private final ExamResultRepository resultRepository;

    public ExamResultController(
            ExamResultRepository resultRepository) {

        this.resultRepository = resultRepository;
    }

    @GetMapping("/{examId}/results")
    public ResponseEntity<List<ExamResult>> getResults(
            @PathVariable String examId) {

        return ResponseEntity.ok(
                resultRepository.findByExamId(examId));
    }
}