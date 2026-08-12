package com.example.backend.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class AdminDashboardResponse {

    private long totalStudents;
    private long totalTrainers;
    private long totalCourses;
    private long totalBatches;
    private long activeLiveSessions;
    private long completedSessions;
    private long totalExams;
    private long totalCertificates;
    private long pendingAssignments;
    private List<RecentRegistrationDTO> recentRegistrations;
    private LocalDateTime generatedAt;

}