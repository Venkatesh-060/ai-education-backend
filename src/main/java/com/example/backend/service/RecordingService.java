package com.example.backend.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.backend.dto.RecordingAnalyticsResponse;
import com.example.backend.dto.RecordingRequest;
import com.example.backend.model.Recording;
import com.example.backend.model.Session;
import com.example.backend.repository.RecordingRepository;
import com.example.backend.repository.SessionRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecordingService {

    private final RecordingRepository recordingRepository;
    private final SessionRepo sessionRepo;

    // Upload Recording Metadata

    public Recording upload(RecordingRequest request) {

        if (request.getTitle() == null ||
                request.getTitle().trim().isEmpty()) {

            throw new RuntimeException("Recording title is required");
        }

        if (request.getVideoUrl() == null ||
                request.getVideoUrl().trim().isEmpty()) {

            throw new RuntimeException("Video URL is required");
        }

        Session session = sessionRepo.findById(request.getSessionId())
                .orElseThrow(() -> new RuntimeException("Session not found"));

        // if (!session.getTrainerId().equals(recording.getTrainerId())) {
        // throw new RuntimeException("Trainer does not belong to this session");
        // }

        if (recordingRepository.existsBySessionIdAndTitle(
                request.getSessionId(),
                request.getTitle())) {

            throw new RuntimeException("Recording already exists");
        }

        Recording recording = new Recording();

        recording.setSessionId(request.getSessionId());
        recording.setBatchId(request.getBatchId());
        recording.setTrainerId(request.getTrainerId());

        recording.setTitle(request.getTitle());
        recording.setDescription(request.getDescription());

        recording.setVideoUrl(request.getVideoUrl());
        recording.setThumbnailUrl(request.getThumbnailUrl());

        recording.setDuration(request.getDuration());
        recording.setFileSize(request.getFileSize());

        recording.setStatus("READY");

        recording.setRecordingDate(LocalDateTime.now());
        recording.setRecordingStartTime(LocalDateTime.now());
        recording.setRecordingEndTime(LocalDateTime.now());

        recording.setDownloadEnabled(
                request.getDownloadEnabled() == null ? true : request.getDownloadEnabled());

        recording.setVisibility(
                request.getVisibility() == null ? "PUBLIC_BATCH" : request.getVisibility());

        recording.setPlaybackCount(0);
        recording.setViewCount(0);
        recording.setUniqueViewers(0);
        recording.setDownloadCount(0);
        recording.setWatchDuration(0L);

        recording.setDeleted(false);

        recording.setCreatedAt(LocalDateTime.now());
        recording.setUpdatedAt(LocalDateTime.now());

        return recordingRepository.save(recording);
    }

    // Get All Recordings

    public Page<Recording> getAll(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        return recordingRepository.findByDeletedFalseOrderByCreatedAtDesc(pageable);
    }

    // Get One Recording

    public Recording get(String id) {

        return recordingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recording not found"));
    }

    // Update Recording

    public Recording update(String id,
            RecordingRequest request) {

        Recording recording = get(id);

        recording.setTitle(request.getTitle());
        recording.setDescription(request.getDescription());

        recording.setThumbnailUrl(request.getThumbnailUrl());

        recording.setVisibility(request.getVisibility());

        recording.setUpdatedAt(LocalDateTime.now());

        return recordingRepository.save(recording);
    }

    // Delete (Soft Delete)

    public void delete(String id) {

        Recording recording = get(id);

        recording.setDeleted(true);

        recording.setUpdatedAt(LocalDateTime.now());

        recordingRepository.save(recording);
    }

    // Search

    public List<Recording> search(String keyword) {

        return recordingRepository.findByTitleContainingIgnoreCase(keyword);
    }

    // Batch Filter

    public List<Recording> byBatch(String batchId) {

        return recordingRepository.findByBatchId(batchId);
    }

    // Trainer Filter

    public List<Recording> byTrainer(String trainerId) {

        return recordingRepository.findByTrainerId(trainerId);
    }

    // Session Filter

    public List<Recording> bySession(String sessionId) {

        return recordingRepository.findBySessionId(sessionId);
    }

    // Play Recording

    public String play(String id) {

        Recording recording = get(id);

        recording.setPlaybackCount(
                recording.getPlaybackCount() == null ? 1 : recording.getPlaybackCount() + 1);

        recording.setViewCount(
                recording.getViewCount() == null ? 1 : recording.getViewCount() + 1);

        // Demo unique viewer count
        recording.setUniqueViewers(
                recording.getUniqueViewers() == null ? 1 : recording.getUniqueViewers() + 1);

        // Assume full video watched
        recording.setWatchDuration(
                recording.getWatchDuration() == null
                        ? recording.getDuration()
                        : recording.getWatchDuration() + recording.getDuration());

        recording.setLastViewedTime(LocalDateTime.now());

        recording.setUpdatedAt(LocalDateTime.now());

        recordingRepository.save(recording);

        return recording.getVideoUrl();
    }

    // Download

    public String download(String id) {

        Recording recording = get(id);

        if (!Boolean.TRUE.equals(recording.getDownloadEnabled())) {
            throw new RuntimeException("Download Disabled");
        }

        recording.setDownloadCount(
                recording.getDownloadCount() == null
                        ? 1
                        : recording.getDownloadCount() + 1);

        recording.setUpdatedAt(LocalDateTime.now());

        recordingRepository.save(recording);

        return recording.getVideoUrl();
    }

    public RecordingAnalyticsResponse analytics(String id) {

        Recording recording = get(id);

        RecordingAnalyticsResponse response = new RecordingAnalyticsResponse();

        response.setRecordingId(recording.getId());
        response.setTitle(recording.getTitle());

        response.setPlaybackCount(recording.getPlaybackCount());
        response.setViewCount(recording.getViewCount());
        response.setUniqueViewers(recording.getUniqueViewers());
        response.setWatchDuration(recording.getWatchDuration());
        response.setDownloadCount(recording.getDownloadCount());

        if (recording.getLastViewedTime() != null) {
            response.setLastViewedTime(
                    recording.getLastViewedTime().toString());
        }

        return response;
    }

    public Recording mostViewed() {

        return recordingRepository
                .findTopByOrderByPlaybackCountDesc();
    }

    public Recording updateStatus(
            String id,
            String status) {

        Recording recording = get(id);

        recording.setStatus(status);

        recording.setUpdatedAt(LocalDateTime.now());

        return recordingRepository.save(recording);
    }

}