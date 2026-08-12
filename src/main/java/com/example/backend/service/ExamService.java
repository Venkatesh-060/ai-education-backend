package com.example.backend.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import com.example.backend.model.Exam;
import com.example.backend.repository.BatchRepo;
import com.example.backend.repository.ExamRepository;

@Service
public class ExamService {

        private final ExamRepository examRepository;
        private final BatchRepo batchRepo;

        public ExamService(
                        ExamRepository examRepository,
                        BatchRepo batchRepo) {

                this.examRepository = examRepository;
                this.batchRepo = batchRepo;
        }

        public Exam createExam(Exam exam) {

                validateExam(exam);

                exam.setId(null);

                if (exam.getStatus() == null ||
                                exam.getStatus().isBlank()) {

                        exam.setStatus("Draft");
                }

                exam.setCreatedAt(LocalDateTime.now());
                exam.setUpdatedAt(LocalDateTime.now());

                return examRepository.save(exam);
        }

        public List<Exam> getAllExams() {

                return examRepository.findAll();
        }

        public Exam getExamById(String id) {

                return examRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Exam not found"));
        }

        public List<Exam> getTrainerExams(String trainerId) {

                return examRepository.findByTrainerId(trainerId);
        }

        public Exam updateExam(
                        String id,
                        Exam updatedExam) {

                Exam existingExam = getExamById(id);

                validateExam(updatedExam);

                existingExam.setExamName(
                                updatedExam.getExamName());

                existingExam.setCourseId(
                                updatedExam.getCourseId());

                existingExam.setCourseName(
                                updatedExam.getCourseName());

                existingExam.setBatchId(
                                updatedExam.getBatchId());

                existingExam.setBatchName(
                                updatedExam.getBatchName());

                existingExam.setDuration(
                                updatedExam.getDuration());

                existingExam.setTotalMarks(
                                updatedExam.getTotalMarks());

                existingExam.setPassingMarks(
                                updatedExam.getPassingMarks());

                existingExam.setExamDate(
                                updatedExam.getExamDate());

                existingExam.setQuestions(
                                updatedExam.getQuestions());

                existingExam.setUpdatedAt(
                                LocalDateTime.now());

                return examRepository.save(existingExam);
        }

        public void deleteExam(String id) {

                if (!examRepository.existsById(id)) {

                        throw new RuntimeException(
                                        "Exam not found");
                }

                examRepository.deleteById(id);
        }

        public Exam publishExam(String id) {

                Exam exam = getExamById(id);

                if ("Completed".equalsIgnoreCase(exam.getStatus())) {
                        throw new RuntimeException(
                                        "Completed exam cannot be published");
                }

                exam.setStatus("Published");
                exam.setUpdatedAt(LocalDateTime.now());

                return examRepository.save(exam);
        }

        public Exam unpublishExam(String id) {

                Exam exam = getExamById(id);

                if ("Completed".equalsIgnoreCase(exam.getStatus())) {
                        throw new RuntimeException(
                                        "Completed exam cannot be unpublished");
                }

                exam.setStatus("Unpublished");
                exam.setUpdatedAt(LocalDateTime.now());

                return examRepository.save(exam);
        }

        public Exam updateStatus(
                        String id,
                        String status) {

                Exam exam = getExamById(id);

                if (!isValidStatus(status)) {

                        throw new RuntimeException(
                                        "Invalid exam status");
                }

                exam.setStatus(status);

                exam.setUpdatedAt(
                                LocalDateTime.now());

                return examRepository.save(exam);
        }

        private boolean isValidStatus(
                        String status) {

                if (status == null) {
                        return false;
                }

                return status.equalsIgnoreCase("Draft")
                                || status.equalsIgnoreCase("Published")
                                || status.equalsIgnoreCase("Ongoing")
                                || status.equalsIgnoreCase("Completed")
                                || status.equalsIgnoreCase("Unpublished");
        }

        private void validateExam(Exam exam) {

                if (exam == null) {

                        throw new RuntimeException(
                                        "Exam data is required");
                }

                if (exam.getExamName() == null ||
                                exam.getExamName().isBlank()) {

                        throw new RuntimeException(
                                        "Exam name is required");
                }

                if (exam.getDuration() == null ||
                                exam.getDuration() <= 0) {

                        throw new RuntimeException(
                                        "Duration must be greater than zero");
                }

                if (exam.getTotalMarks() == null ||
                                exam.getTotalMarks() <= 0) {

                        throw new RuntimeException(
                                        "Total marks must be greater than zero");
                }

                if (exam.getPassingMarks() == null ||
                                exam.getPassingMarks() < 0) {

                        throw new RuntimeException(
                                        "Passing marks are required");
                }

                if (exam.getPassingMarks() > exam.getTotalMarks()) {

                        throw new RuntimeException(
                                        "Passing marks cannot exceed total marks");
                }

                if (exam.getBatchId() == null ||
                                exam.getBatchId().isBlank()) {

                        throw new RuntimeException(
                                        "Batch is required");
                }

                if (exam.getCourseId() == null ||
                                exam.getCourseId().isBlank()) {

                        throw new RuntimeException(
                                        "Course is required");
                }

                if (exam.getExamDate() == null) {

                        throw new RuntimeException(
                                        "Exam date is required");
                }
        }

        public int getTotalStudents(Exam exam) {

                if (exam == null) {
                        return 0;
                }

                String batchId = exam.getBatchId();

                if (batchId == null ||
                                batchId.isBlank()) {

                        return 0;
                }

                return batchRepo.findById(batchId)
                                .map(batch -> {

                                        if (batch.getStudentIds() == null) {
                                                return 0;
                                        }

                                        return batch.getStudentIds().size();
                                })
                                .orElse(0);
        }
}