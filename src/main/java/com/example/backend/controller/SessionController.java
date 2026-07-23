package com.example.backend.controller;

import java.util.List;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import com.example.backend.model.Session;
import com.example.backend.repository.SessionRepo;

@RestController
@RequestMapping("/api/session")
@CrossOrigin(origins = "http://localhost:5173")
public class SessionController {

    private final SessionRepo sessionRepo;

    public SessionController(SessionRepo sessionRepo) {
        this.sessionRepo = sessionRepo;
    }

    @PostMapping("/create")
    public String createSession(@RequestBody @NonNull Session session) {

        sessionRepo.save(session);

        return "Session Created Successfully";
    }

    @GetMapping("/all")
    public List<Session> getAllSessions() {

        return sessionRepo.findAll();
    }

    @GetMapping("/{id}")
    public Session getSession(@PathVariable @NonNull String id) {

        return sessionRepo.findById(id).orElse(null);
    }

    @DeleteMapping("/{id}")
    public String deleteSession(@PathVariable @NonNull String id) {

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

        Session oldSession = sessionRepo.findById(id).orElse(null);

        if (oldSession == null) {
            return null;
        }

        oldSession.setSessionName(session.getSessionName());
        oldSession.setBatchName(session.getBatchName());
        oldSession.setSessionDate(session.getSessionDate());
        oldSession.setStartTime(session.getStartTime());
        oldSession.setEndTime(session.getEndTime());

        return sessionRepo.save(oldSession);
    }
}