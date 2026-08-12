package com.example.backend.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.backend.model.Batch;
import com.example.backend.service.BatchService;

@RestController
@RequestMapping("/api/trainer/batches")
@CrossOrigin(origins = "http://localhost:5173")
public class TrainerBatchController {

    private final BatchService batchService;

    public TrainerBatchController(BatchService batchService) {
        this.batchService = batchService;
    }

    @GetMapping
    public ResponseEntity<List<Batch>> getTrainerBatches(
            @RequestParam String trainerId) {

        return ResponseEntity.ok(
                batchService.getBatchesByTrainer(trainerId));
    }
}