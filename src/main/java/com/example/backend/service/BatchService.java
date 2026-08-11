package com.example.backend.service;

import java.util.List;
import com.example.backend.dto.BatchStatisticsDTO;
import com.example.backend.model.Batch;
import com.example.backend.model.User;

public interface BatchService {

    Batch createBatch(Batch batch);

    List<Batch> getAllBatches();

    Batch getBatch(String id);

    Batch updateBatch(String id, Batch batch);

    void deleteBatch(String id);

    Batch assignTrainer(String batchId, String trainerId);

    Batch allocateStudents(String batchId, List<String> studentIds);

    Batch removeStudent(String batchId, String studentId);

    List<User> getStudents();

    List<User> getTrainers();

    BatchStatisticsDTO getStatistics();

    List<Batch> getBatchesByTrainer(String trainerId);
}