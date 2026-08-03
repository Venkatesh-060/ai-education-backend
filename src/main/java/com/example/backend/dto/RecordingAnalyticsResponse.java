package com.example.backend.dto;

import lombok.Data;

@Data
public class RecordingAnalyticsResponse {
    private String recordingId;
    private String title;
    private Integer playbackCount;
    private Integer viewCount;
    private Integer uniqueViewers;
    private Long watchDuration;
    private Integer downloadCount;
    private String lastViewedTime;

}