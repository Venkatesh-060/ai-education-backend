package com.example.backend.model;

import java.time.LocalDateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "recordings")
public class Recording {

    @Id
    private String id;
    private String sessionId;
    private String batchId;
    private String trainerId;
    private String title;
    private String description;
    private String videoUrl;
    private String thumbnailUrl;
    private Long duration;
    private Double fileSize;
    private String status = "PROCESSING";
    private LocalDateTime recordingStartTime;
    private LocalDateTime recordingEndTime;
    private LocalDateTime recordingDate;
    private Integer playbackCount = 0;
    private Integer viewCount = 0;
    private Integer uniqueViewers = 0;
    private Long watchDuration = 0L;
    private Integer downloadCount = 0;
    private LocalDateTime lastViewedTime;
    private Boolean downloadEnabled = true;
    private String visibility = "PUBLIC_BATCH";
    private Boolean deleted = false;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}