package com.example.backend.controller;

import java.util.List;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import com.example.backend.model.Session;
import com.example.backend.repository.AttendanceRepo;
import com.example.backend.repository.ParticipantRepo;
import com.example.backend.repository.SessionRepo;
import com.example.backend.repository.UserRepo;

@RestController
@RequestMapping("/api/session")
@CrossOrigin(origins = "http://localhost:5173")
public class SessionController {

    private final SessionRepo sessionRepo;
    private final ParticipantRepo participantRepo;
    private final AttendanceRepo attendanceRepo;
    private final UserRepo userRepo;

    public SessionController(
            SessionRepo sessionRepo,
            ParticipantRepo participantRepo,
            AttendanceRepo attendanceRepo,
            UserRepo userRepo) {

        this.sessionRepo = sessionRepo;
        this.participantRepo = participantRepo;
        this.attendanceRepo = attendanceRepo;
        this.userRepo = userRepo;
    }

    @PostMapping("/create")
    public String createSession(
            @RequestBody @NonNull Session session) {

        if (session.getStatus() == null ||
                session.getStatus().isBlank()) {

            session.setStatus("Upcoming");
        }
        sessionRepo.save(session);

        return "Session Created Successfully";
    }

    @GetMapping("/all")
    public List<Session> getAllSessions() {

        return sessionRepo.findAll();
    }

    @GetMapping("/{id}")
    public Session getSession(
            @PathVariable @NonNull String id) {

        return sessionRepo.findById(id).orElse(null);
    }

    @DeleteMapping("/{id}")
    public String deleteSession(
            @PathVariable @NonNull String id) {

        if (!sessionRepo.existsById(id)) {
            return "Session Not Found";
        }

        sessionRepo.deleteById(id);

        return "Session Deleted Successfully";
    }

    @GetMapping("/trainer/{trainerId}")
    public List<Session> getTrainerSessions(
            @PathVariable String trainerId) {

        return sessionRepo.findByTrainerId(trainerId);
    }

    @PutMapping("/{id}")
    public Session updateSession(
            @PathVariable @NonNull String id,
            @RequestBody @NonNull Session session) {

        Session oldSession = sessionRepo.findById(id)
                .orElse(null);

        if (oldSession == null) {
            return null;
        }

        oldSession.setSessionName(
                session.getSessionName());

        oldSession.setDescription(
                session.getDescription());

        oldSession.setBatchName(
                session.getBatchName());

        oldSession.setSessionDate(
                session.getSessionDate());

        oldSession.setStartTime(
                session.getStartTime());

        oldSession.setEndTime(
                session.getEndTime());

        return sessionRepo.save(oldSession);
    }

    @PutMapping("/{id}/start")
    public String startSession(
            @PathVariable String id) {

        Session session = sessionRepo.findById(id)
                .orElse(null);

        if (session == null) {
            return "Session Not Found";
        }
        if ("Live".equalsIgnoreCase(session.getStatus())) {
            return "Session Already Live";
        }

        if ("Completed".equalsIgnoreCase(session.getStatus())) {
            return "Completed Session Cannot Be Started";
        }

        session.setStatus("Live");

        session.setActualStartTime(
                java.time.LocalDateTime.now().toString());

        session.setActualEndTime(null);

        sessionRepo.save(session);

        return "Session Started Successfully";
    }

    @PutMapping("/{id}/end")
    public String endSession(
            @PathVariable @NonNull String id) {

        Session session = sessionRepo.findById(id)
                .orElse(null);

        if (session == null) {
            return "Session Not Found";
        }

        if ("Completed".equalsIgnoreCase(session.getStatus())) {
            return "Session Already Completed";
        }

        session.setStatus("Completed");

        session.setActualEndTime(
                java.time.LocalDateTime.now().toString());

        sessionRepo.save(session);

        return "Session Ended Successfully";
    }
}