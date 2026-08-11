package com.example.backend.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.backend.dto.BatchStatisticsDTO;
import com.example.backend.model.Batch;
import com.example.backend.model.User;
import com.example.backend.service.BatchService;

@RestController
@RequestMapping("/api/admin/batches")
@CrossOrigin(origins = "http://localhost:5173")
public class BatchController {

    private final BatchService batchService;

    public BatchController(BatchService batchService) {
        this.batchService = batchService;
    }

    // ==========================================
    // CREATE BATCH
    // POST /api/admin/batches
    // ==========================================

    @PostMapping
    public ResponseEntity<Batch> create(
            @RequestBody Batch batch) {

        return ResponseEntity.ok(
                batchService.createBatch(batch));
    }

    // ==========================================
    // GET ALL BATCHES
    // GET /api/admin/batches
    // ==========================================

    @GetMapping
    public ResponseEntity<List<Batch>> getAll() {

        return ResponseEntity.ok(
                batchService.getAllBatches());
    }

    // ==========================================
    // GET ALL STUDENTS
    // GET /api/admin/batches/students
    // ==========================================

    @GetMapping("/students")
    public ResponseEntity<List<User>> getStudents() {

        return ResponseEntity.ok(
                batchService.getStudents());
    }

    // ==========================================
    // GET ALL TRAINERS
    // GET /api/admin/batches/trainers
    // ==========================================

    @GetMapping("/trainers")
    public ResponseEntity<List<User>> getTrainers() {

        return ResponseEntity.ok(
                batchService.getTrainers());
    }

    // ==========================================
    // BATCH STATISTICS
    // GET /api/admin/batches/statistics
    // ==========================================

    @GetMapping("/statistics")
    public ResponseEntity<BatchStatisticsDTO> statistics() {

        return ResponseEntity.ok(
                batchService.getStatistics());
    }

    // ==========================================
    // GET SINGLE BATCH
    // GET /api/admin/batches/{id}
    // ==========================================

    @GetMapping("/{id}")
    public ResponseEntity<Batch> get(
            @PathVariable String id) {

        return ResponseEntity.ok(
                batchService.getBatch(id));
    }

    // ==========================================
    // UPDATE BATCH
    // PUT /api/admin/batches/{id}
    // ==========================================

    @PutMapping("/{id}")
    public ResponseEntity<Batch> update(
            @PathVariable String id,
            @RequestBody Batch batch) {

        return ResponseEntity.ok(
                batchService.updateBatch(id, batch));
    }

    // ==========================================
    // DELETE BATCH
    // DELETE /api/admin/batches/{id}
    // ==========================================

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
            @PathVariable String id) {

        batchService.deleteBatch(id);

        return ResponseEntity.ok(
                "Batch deleted successfully");
    }

    // ==========================================
    // ASSIGN TRAINER
    // PUT /api/admin/batches/{batchId}/trainer/{trainerId}
    // ==========================================

    @PutMapping("/{batchId}/trainer/{trainerId}")
    public ResponseEntity<Batch> assignTrainer(
            @PathVariable String batchId,
            @PathVariable String trainerId) {

        return ResponseEntity.ok(
                batchService.assignTrainer(
                        batchId,
                        trainerId));
    }

    // ==========================================
    // ALLOCATE STUDENTS
    // PUT /api/admin/batches/{batchId}/students
    // ==========================================

    @PutMapping("/{batchId}/students")
    public ResponseEntity<Batch> allocateStudents(
            @PathVariable String batchId,
            @RequestBody List<String> studentIds) {

        return ResponseEntity.ok(
                batchService.allocateStudents(
                        batchId,
                        studentIds));
    }

    // ==========================================
    // REMOVE STUDENT
    // DELETE /api/admin/batches/{batchId}/students/{studentId}
    // ==========================================

    @DeleteMapping("/{batchId}/students/{studentId}")
    public ResponseEntity<Batch> removeStudent(
            @PathVariable String batchId,
            @PathVariable String studentId) {

        return ResponseEntity.ok(
                batchService.removeStudent(
                        batchId,
                        studentId));
    }
}