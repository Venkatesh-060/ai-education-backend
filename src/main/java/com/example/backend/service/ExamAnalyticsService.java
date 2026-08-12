package com.example.backend.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import com.example.backend.model.Batch;
import com.example.backend.model.Exam;
import com.example.backend.model.ExamResult;
import com.example.backend.repository.BatchRepo;
import com.example.backend.repository.ExamRepository;
import com.example.backend.repository.ExamResultRepository;

@Service
public class ExamAnalyticsService {

    private final ExamRepository examRepository;
    private final ExamResultRepository resultRepository;
    private final BatchRepo batchRepo;

    public ExamAnalyticsService(
            ExamRepository examRepository,
            ExamResultRepository resultRepository,
            BatchRepo batchRepo) {

        this.examRepository = examRepository;
        this.resultRepository = resultRepository;
        this.batchRepo = batchRepo;
    }

    public Map<String, Object> getAnalytics(String examId) {

        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found"));

        List<ExamResult> results = resultRepository.findByExamId(examId);
        int totalStudents = getTotalStudents(exam);
        int attemptedStudents = results.size();
        int completedStudents = 0;
        int passedStudents = 0;
        int failedStudents = 0;
        double totalScore = 0;
        double highestScore = 0;
        double lowestScore = results.isEmpty()
                ? 0
                : Double.MAX_VALUE;

        for (ExamResult result : results) {

            double score = result.getObtainedMarks() == null
                    ? 0
                    : result.getObtainedMarks();

            totalScore += score;
            highestScore = Math.max(highestScore, score);
            lowestScore = Math.min(lowestScore, score);

            if ("Completed".equalsIgnoreCase(
                    result.getCompletionStatus())) {

                completedStudents++;
            }

            if ("PASS".equalsIgnoreCase(
                    result.getResultStatus())) {

                passedStudents++;

            } else if ("FAIL".equalsIgnoreCase(
                    result.getResultStatus())) {

                failedStudents++;
            }
        }

        double averageScore = attemptedStudents == 0
                ? 0
                : totalScore / attemptedStudents;

        double passPercentage = attemptedStudents == 0
                ? 0
                : ((double) passedStudents
                        / attemptedStudents) * 100;

        double failPercentage = attemptedStudents == 0
                ? 0
                : ((double) failedStudents
                        / attemptedStudents) * 100;
        double completionRate = totalStudents == 0
                ? 0
                : ((double) completedStudents
                        / totalStudents) * 100;

        Map<String, Object> analytics = new HashMap<>();

        analytics.put("examId", examId);

        analytics.put(
                "examName",
                exam.getExamName());

        analytics.put(
                "totalStudents",
                totalStudents);

        analytics.put(
                "attemptedStudents",
                attemptedStudents);

        analytics.put(
                "completedStudents",
                completedStudents);

        analytics.put(
                "averageScore",
                round(averageScore));

        analytics.put(
                "highestScore",
                highestScore);

        analytics.put(
                "lowestScore",
                lowestScore);

        analytics.put(
                "passedStudents",
                passedStudents);

        analytics.put(
                "failedStudents",
                failedStudents);

        analytics.put(
                "passPercentage",
                round(passPercentage));

        analytics.put(
                "failPercentage",
                round(failPercentage));

        analytics.put(
                "completionRate",
                round(completionRate));

        return analytics;
    }

    private int getTotalStudents(Exam exam) {

        if (exam.getBatchId() == null ||
                exam.getBatchId().isBlank()) {

            return 0;
        }

        Batch batch = batchRepo
                .findById(exam.getBatchId())
                .orElse(null);

        if (batch == null) {
            return 0;
        }

        if (batch.getStudentIds() == null) {
            return 0;
        }

        return batch.getStudentIds().size();
    }

    private double round(double value) {

        return Math.round(value * 100.0) / 100.0;
    }
}