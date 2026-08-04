package com.example.backend.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class AdminDashboardResponse {

    // User Statistics
    private long totalStudents;
    private long totalTrainers;

    // Course Statistics
    private long totalCourses;
    private long totalBatches;

    // Session Statistics
    private long activeLiveSessions;
    private long completedSessions;

    // Other Statistics
    private long totalExams;
    private long totalCertificates;
    private long pendingAssignments;

    // Recent Registrations
    private List<RecentRegistrationDTO> recentRegistrations;

    // Dashboard Generated Time
    private LocalDateTime generatedAt;

}