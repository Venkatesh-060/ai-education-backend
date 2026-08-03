package com.example.backend.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.backend.model.Recording;

public interface RecordingRepository
        extends MongoRepository<Recording, String> {
    Page<Recording> findByDeletedFalseOrderByCreatedAtDesc(
            Pageable pageable);

    List<Recording> findByBatchId(String batchId);

    List<Recording> findByTrainerId(String trainerId);

    List<Recording> findBySessionId(String sessionId);

    List<Recording> findByTitleContainingIgnoreCase(String keyword);

    boolean existsBySessionIdAndTitle(
            String sessionId,
            String title);

    Recording findTopByOrderByPlaybackCountDesc();

}