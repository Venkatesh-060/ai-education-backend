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
    public String create(@RequestBody Batch batch) {

        batchRepo.save(batch);

        return "Batch Created Successfully";
    }

    @GetMapping("/all")
    public List<Batch> getAll() {

        return batchRepo.findAll();
    }

    @GetMapping("/{id}")
    public Batch get(@PathVariable String id) {

        return batchRepo.findById(id).orElse(null);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable String id) {

        batchRepo.deleteById(id);

        return "Batch Deleted Successfully";
    }
}