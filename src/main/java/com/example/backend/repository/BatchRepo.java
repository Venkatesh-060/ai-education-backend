package com.example.backend.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.backend.model.Batch;

public interface BatchRepo extends MongoRepository<Batch, String> {

    List<Batch> findByTrainerId(String trainerId);
    boolean existsByBatchName(String batchName);
    long countByStatus(String status);
}