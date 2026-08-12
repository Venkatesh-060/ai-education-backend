package com.example.backend.controller;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.backend.dto.AdminLiveSessionResponse;
import com.example.backend.dto.SessionStatisticsResponse;
import com.example.backend.model.Attendance;
import com.example.backend.model.Participant;
import com.example.backend.model.Session;
import com.example.backend.repository.AttendanceRepo;
import com.example.backend.repository.ParticipantRepo;
import com.example.backend.repository.SessionRepo;
import com.example.backend.repository.UserRepo;

@RestController
@RequestMapping("/api/admin/live-sessions")
@CrossOrigin(origins = "http://localhost:5173")
public class AdminLiveSessionController {

    private final SessionRepo sessionRepo;
    private final ParticipantRepo participantRepo;
    private final AttendanceRepo attendanceRepo;
    private final UserRepo userRepo;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AdminLiveSessionController(
            SessionRepo sessionRepo,
            ParticipantRepo participantRepo,
            AttendanceRepo attendanceRepo,
            UserRepo userRepo) {

        this.sessionRepo = sessionRepo;
        this.participantRepo = participantRepo;
        this.attendanceRepo = attendanceRepo;
        this.userRepo = userRepo;
    }

    @GetMapping
    public List<AdminLiveSessionResponse> getLiveSessions() {

        List<Session> sessions = sessionRepo.findAll();

        List<AdminLiveSessionResponse> response = new ArrayList<>();

        for (Session session : sessions) {

            if (!"Live".equalsIgnoreCase(session.getStatus())) {
                continue;
            }

            AdminLiveSessionResponse dto = buildSessionResponse(session);

            response.add(dto);
        }

        return response;
    }

    @GetMapping("/{sessionId}")
    public ResponseEntity<AdminLiveSessionResponse> getLiveSession(
            @PathVariable String sessionId) {

        Session session = sessionRepo.findById(sessionId)
                .orElse(null);

        if (session == null) {
            return ResponseEntity.notFound().build();
        }

        if (!"Live".equalsIgnoreCase(session.getStatus())) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(buildSessionResponse(session));
    }

    @PutMapping("/{sessionId}/end")
    public String endSession(
            @PathVariable String sessionId) {

        Session session = sessionRepo.findById(sessionId)
                .orElse(null);

        if (session == null) {
            return "Session Not Found";
        }

        if (!"Live".equalsIgnoreCase(session.getStatus())) {
            return "Session is not currently live";
        }

        LocalDateTime endTime = LocalDateTime.now();

        session.setStatus("Completed");
        session.setActualEndTime(endTime.toString());

        sessionRepo.save(session);

        List<Participant> participants = participantRepo.findBySessionIdAndStatus(
                sessionId,
                "ACTIVE");

        for (Participant participant : participants) {

            participant.setStatus("DISCONNECTED");
            participant.setLeftAt(endTime);

            participantRepo.save(participant);

            List<Attendance> attendanceList = attendanceRepo.findByUserIdAndSessionId(
                    participant.getUserId(),
                    sessionId);

            if (!attendanceList.isEmpty()) {

                Attendance attendance = attendanceList.get(0);

                if (attendance.getJoinTime() != null) {

                    try {

                        LocalDateTime joinTime = LocalDateTime.parse(
                                attendance.getJoinTime());

                        long minutes = Duration.between(
                                joinTime,
                                endTime)
                                .toMinutes();

                        attendance.setLeaveTime(
                                endTime.toString());

                        attendance.setDuration(minutes);

                        attendance.setUpdatedAt(
                                LocalDateTime.now());

                        if (minutes < 30) {
                            attendance.setStatus("Left Early");
                        }

                        attendanceRepo.save(attendance);

                    } catch (Exception e) {

                        System.out.println(
                                "Attendance update error for user "
                                        + participant.getUserId()
                                        + ": "
                                        + e.getMessage());
                    }
                }
            }
        }

        return "Live Session Ended Successfully";
    }

    @GetMapping("/{sessionId}/statistics")
    public SessionStatisticsResponse getStatistics(
            @PathVariable String sessionId) {

        Session session = sessionRepo.findById(sessionId)
                .orElse(null);

        if (session == null) {
            return null;
        }

        SessionStatisticsResponse response = new SessionStatisticsResponse();
        response.setSessionId(session.getId());
        response.setSessionName(
                session.getSessionName());
        response.setStatus(
                session.getStatus());
        long duration = calculateDuration(session);
        response.setDurationMinutes(duration);
        long totalParticipants = participantRepo.countBySessionId(sessionId);
        long activeParticipants = participantRepo.countBySessionIdAndStatus(
                sessionId,
                "ACTIVE");
        long disconnectedParticipants = participantRepo.countBySessionIdAndStatus(
                sessionId,
                "DISCONNECTED");
        long present = attendanceRepo.countBySessionIdAndStatus(
                sessionId,
                "Present");

        long late = attendanceRepo.countBySessionIdAndStatus(
                sessionId,
                "Late");

        long leftEarly = attendanceRepo.countBySessionIdAndStatus(
                sessionId,
                "Left Early");

        long absent = attendanceRepo.countBySessionIdAndStatus(
                sessionId,
                "Absent");

        response.setTotalParticipants(
                totalParticipants);

        response.setActiveParticipants(
                activeParticipants);

        response.setDisconnectedParticipants(
                disconnectedParticipants);
        response.setPresent(present);

        response.setLate(late);

        response.setLeftEarly(leftEarly);

        response.setAbsent(absent);

        long attendanceRecords = attendanceRepo
                .findBySessionId(sessionId)
                .size();

        long attended = present + late + leftEarly;

        double attendancePercentage = 0.0;

        if (attendanceRecords > 0) {

            attendancePercentage = ((double) attended /
                    attendanceRecords) * 100;
        }

        attendancePercentage = Math.round(
                attendancePercentage * 100.0) / 100.0;

        response.setAttendancePercentage(
                attendancePercentage);

        return response;
    }

    private AdminLiveSessionResponse buildSessionResponse(
            Session session) {

        AdminLiveSessionResponse response = new AdminLiveSessionResponse();

        response.setId(session.getId());
        response.setSessionName(session.getSessionName());

        response.setTrainerId(session.getTrainerId());

        response.setBatchName(session.getBatchName());

        response.setSessionDate(session.getSessionDate());

        response.setStartTime(session.getStartTime());

        response.setEndTime(session.getEndTime());

        response.setStatus(session.getStatus());

        response.setSessionName(session.getSessionName());
        response.setDescription(session.getDescription());

        response.setActualStartTime(
                session.getActualStartTime());

        response.setActualEndTime(
                session.getActualEndTime());

        if (session.getTrainerId() != null) {

            userRepo.findById(session.getTrainerId())
                    .ifPresent(user -> response.setTrainerName(
                            user.getFirstName()
                                    + " "
                                    + user.getLastName()));
        }

        long participantCount = participantRepo.countBySessionId(
                session.getId());

        long activeParticipants = participantRepo.countBySessionIdAndStatus(
                session.getId(),
                "ACTIVE");

        response.setParticipantCount(
                participantCount);

        response.setActiveParticipants(
                activeParticipants);

        response.setDurationMinutes(
                calculateDuration(session));

        return response;
    }

    private long calculateDuration(Session session) {

        if (session.getActualStartTime() == null) {
            return 0;
        }

        try {

            LocalDateTime start = LocalDateTime.parse(
                    session.getActualStartTime());

            LocalDateTime end;

            if (session.getActualEndTime() != null) {

                end = LocalDateTime.parse(
                        session.getActualEndTime());

            } else {

                end = LocalDateTime.now();
            }

            return Duration.between(
                    start,
                    end).toMinutes();

        } catch (Exception e) {

            return 0;
        }
    }

    @GetMapping("/{sessionId}/participants")
    public ResponseEntity<List<Participant>> getLiveParticipants(
            @PathVariable String sessionId) {

        Session session = sessionRepo.findById(sessionId)
                .orElse(null);

        if (session == null) {
            return ResponseEntity.notFound().build();
        }

        List<Participant> participants = participantRepo.findBySessionId(sessionId);

        return ResponseEntity.ok(participants);
    }

    @GetMapping("/{sessionId}/attendance")
    public ResponseEntity<List<Attendance>> getLiveAttendance(
            @PathVariable String sessionId) {

        Session session = sessionRepo.findById(sessionId)
                .orElse(null);

        if (session == null) {
            return ResponseEntity.notFound().build();
        }

        List<Attendance> attendance = attendanceRepo.findBySessionId(sessionId);

        return ResponseEntity.ok(attendance);
    }
}