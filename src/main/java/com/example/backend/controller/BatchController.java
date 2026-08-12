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

    @PostMapping
    public ResponseEntity<Batch> create(
            @RequestBody Batch batch) {

        return ResponseEntity.ok(
                batchService.createBatch(batch));
    }

    @GetMapping
    public ResponseEntity<List<Batch>> getAll() {

        return ResponseEntity.ok(
                batchService.getAllBatches());
    }

    @GetMapping("/students")
    public ResponseEntity<List<User>> getStudents() {

        return ResponseEntity.ok(
                batchService.getStudents());
    }


    @GetMapping("/trainers")
    public ResponseEntity<List<User>> getTrainers() {

        return ResponseEntity.ok(
                batchService.getTrainers());
    }

    @GetMapping("/statistics")
    public ResponseEntity<BatchStatisticsDTO> statistics() {

        return ResponseEntity.ok(
                batchService.getStatistics());
    }


    @GetMapping("/{id}")
    public ResponseEntity<Batch> get(
            @PathVariable String id) {

        return ResponseEntity.ok(
                batchService.getBatch(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Batch> update(
            @PathVariable String id,
            @RequestBody Batch batch) {

        return ResponseEntity.ok(
                batchService.updateBatch(id, batch));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
            @PathVariable String id) {

        batchService.deleteBatch(id);

        return ResponseEntity.ok(
                "Batch deleted successfully");
    }

    @PutMapping("/{batchId}/trainer/{trainerId}")
    public ResponseEntity<Batch> assignTrainer(
            @PathVariable String batchId,
            @PathVariable String trainerId) {

        return ResponseEntity.ok(
                batchService.assignTrainer(
                        batchId,
                        trainerId));
    }

    @PutMapping("/{batchId}/students")
    public ResponseEntity<Batch> allocateStudents(
            @PathVariable String batchId,
            @RequestBody List<String> studentIds) {

        return ResponseEntity.ok(
                batchService.allocateStudents(
                        batchId,
                        studentIds));
    }

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