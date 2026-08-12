package com.example.backend.controller;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.backend.dto.RecordingAnalyticsResponse;
import com.example.backend.dto.RecordingRequest;
import com.example.backend.model.Recording;
import com.example.backend.service.RecordingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/recordings")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RecordingController {

    private final RecordingService service;

    @PostMapping
    public Recording upload(

            @Valid @RequestBody RecordingRequest request) {

        return service.upload(request);
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadVideo(
            @RequestParam("file") MultipartFile file) throws IOException {

        String uploadDir = "uploads/";

        Files.createDirectories(Paths.get(uploadDir));

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        Path path = Paths.get(uploadDir, fileName);

        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        String videoUrl = "http://localhost:8080/uploads/" + fileName;

        return ResponseEntity.ok(videoUrl);
    }


    @GetMapping
    public Page<Recording> getAll(

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size) {

        return service.getAll(page, size);
    }


    @GetMapping("/{id}")
    public Recording get(

            @PathVariable String id) {

        return service.get(id);
    }


    @PutMapping("/{id}")
    public Recording update(

            @PathVariable String id,

            @RequestBody RecordingRequest request) {

        return service.update(id, request);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(

            @PathVariable String id) {

        service.delete(id);

        return ResponseEntity.ok("Recording Deleted Successfully");
    }

    @GetMapping("/search")
    public List<Recording> search(

            @RequestParam String keyword) {

        return service.search(keyword);
    }

    @GetMapping("/batch/{batchId}")
    public List<Recording> batch(

            @PathVariable String batchId) {

        return service.byBatch(batchId);
    }

    @GetMapping("/trainer/{trainerId}")
    public List<Recording> trainer(

            @PathVariable String trainerId) {

        return service.byTrainer(trainerId);
    }

    @GetMapping("/session/{sessionId}")
    public List<Recording> session(

            @PathVariable String sessionId) {

        return service.bySession(sessionId);
    }

    @GetMapping("/play/{id}")
    public ResponseEntity<String> play(

            @PathVariable String id) {

        return ResponseEntity.ok(

                service.play(id));
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<String> download(

            @PathVariable String id) {

        return ResponseEntity.ok(

                service.download(id));
    }

    @GetMapping("/analytics/{id}")
    public RecordingAnalyticsResponse analytics(
            @PathVariable String id) {

        return service.analytics(id);
    }

    @GetMapping("/most-viewed")
    public Recording mostViewed() {

        return service.mostViewed();
    }

    @PutMapping("/{id}/status")
    public Recording updateStatus(

            @PathVariable String id,

            @RequestParam String status) {

        return service.updateStatus(id, status);
    }

}