package com.example.backend.controller;

import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.backend.model.Exam;
import com.example.backend.service.ExamAnalyticsService;
import com.example.backend.service.ExamService;

@RestController
@RequestMapping("/api/exams")
@CrossOrigin(origins = "http://localhost:5173")
public class ExamController {

    private final ExamService examService;
    private final ExamAnalyticsService analyticsService;

    public ExamController(
            ExamService examService,
            ExamAnalyticsService analyticsService) {

        this.examService = examService;
        this.analyticsService = analyticsService;
    }

    @PostMapping
    public ResponseEntity<Exam> createExam(
            @RequestBody Exam exam) {

        return ResponseEntity.ok(
                examService.createExam(exam));
    }

    @GetMapping
    public ResponseEntity<List<Exam>> getAllExams() {

        return ResponseEntity.ok(
                examService.getAllExams());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Exam> getExam(
            @PathVariable String id) {

        return ResponseEntity.ok(
                examService.getExamById(id));
    }

    @GetMapping("/trainer/{trainerId}")
    public ResponseEntity<List<Exam>> getTrainerExams(
            @PathVariable String trainerId) {

        return ResponseEntity.ok(
                examService.getTrainerExams(trainerId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Exam> updateExam(
            @PathVariable String id,
            @RequestBody Exam exam) {

        return ResponseEntity.ok(
                examService.updateExam(id, exam));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteExam(
            @PathVariable String id) {

        examService.deleteExam(id);

        return ResponseEntity.ok(
                "Exam deleted successfully");
    }

    @PutMapping("/{id}/publish")
    public ResponseEntity<Exam> publishExam(
            @PathVariable String id) {

        return ResponseEntity.ok(
                examService.publishExam(id));
    }

    @PutMapping("/{id}/unpublish")
    public ResponseEntity<Exam> unpublishExam(
            @PathVariable String id) {

        return ResponseEntity.ok(
                examService.unpublishExam(id));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Exam> updateStatus(
            @PathVariable String id,
            @RequestParam String status) {

        return ResponseEntity.ok(
                examService.updateStatus(id, status));
    }

    @GetMapping("/{id}/analytics")
    public ResponseEntity<Map<String, Object>> getAnalytics(
            @PathVariable String id) {

        return ResponseEntity.ok(
                analyticsService.getAnalytics(id));
    }
}