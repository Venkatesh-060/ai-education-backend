package com.example.backend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.backend.dto.AdminDashboardResponse;
import com.example.backend.dto.RecentRegistrationDTO;
import com.example.backend.repository.BatchRepo;
import com.example.backend.repository.SessionRepo;
import com.example.backend.repository.UserRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminDashboardService {

    private final UserRepo userRepo;
    private final SessionRepo sessionRepo;
    private final BatchRepo batchRepo;

    public AdminDashboardResponse getDashboard() {

        AdminDashboardResponse response = new AdminDashboardResponse();

        response.setTotalStudents(
                userRepo.findByRole("STUDENT").size());

        response.setTotalTrainers(
                userRepo.findByRole("TRAINER").size());

        response.setTotalBatches(
                batchRepo.count());

        response.setActiveLiveSessions(
                sessionRepo.countByStatus("LIVE"));

        response.setCompletedSessions(
                sessionRepo.countByStatus("COMPLETED"));

        // Modules not developed yet
        response.setTotalCourses(0);
        response.setTotalExams(0);
        response.setTotalCertificates(0);
        response.setPendingAssignments(0);

        List<RecentRegistrationDTO> recent = userRepo.findAll()
                .stream()
                .limit(5)
                .map(user -> new RecentRegistrationDTO(
                        user.getFirstName() + " " + user.getLastName(),
                        user.getEmail(),
                        user.getRole()))
                .collect(Collectors.toList());

        response.setRecentRegistrations(recent);

        response.setGeneratedAt(LocalDateTime.now());

        return response;
    }
}