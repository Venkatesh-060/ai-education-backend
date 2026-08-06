package com.example.backend.controller;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import com.example.backend.model.Batch;
import com.example.backend.repository.BatchRepo;

@RestController
@RequestMapping("/api/batch")
@CrossOrigin(origins = "http://localhost:5173")
public class BatchController {

    private final BatchRepo batchRepo;

    public BatchController(BatchRepo batchRepo) {
        this.batchRepo = batchRepo;
    }

    @PostMapping("/create")
    public Batch create(@RequestBody Batch batch) {

        batch.setStatus("ACTIVE");
        return batchRepo.save(batch);
    }

    @GetMapping("/all")
    public List<Batch> getAll() {
        return batchRepo.findAll();
    }

    @GetMapping("/{id}")
    public Batch get(@PathVariable String id) {

        return batchRepo.findById(id).orElse(null);

    }

    @PutMapping("/{id}")
    public Batch update(
            @PathVariable String id,
            @RequestBody Batch batch) {

        Batch old = batchRepo.findById(id).orElse(null);
        if (old == null) {
            return null;
        }

        old.setBatchName(batch.getBatchName());
        old.setCourseName(batch.getCourseName());
        old.setTrainerId(batch.getTrainerId());
        old.setTrainerName(batch.getTrainerName());
        old.setStartDate(batch.getStartDate());
        old.setEndDate(batch.getEndDate());
        old.setDescription(batch.getDescription());
        old.setStatus(batch.getStatus());
        return batchRepo.save(old);

    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable String id) {

        batchRepo.deleteById(id);
        return "Batch Deleted";

    }

}