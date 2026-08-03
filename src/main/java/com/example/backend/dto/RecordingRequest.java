package com.example.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RecordingRequest {

    @NotBlank(message = "Session Id is required")
    private String sessionId;
    @NotBlank(message = "Batch Id is required")
    private String batchId;
    @NotBlank(message = "Trainer Id is required")
    private String trainerId;
    @NotBlank(message = "Title is required")
    private String title;
    private String description;
    @NotBlank(message = "Video URL is required")
    private String videoUrl;
    private String thumbnailUrl;
    private Long duration;
    private Double fileSize;
    private Boolean downloadEnabled;
    private String visibility;

}