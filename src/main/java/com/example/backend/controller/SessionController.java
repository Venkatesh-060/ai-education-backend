package com.example.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.backend.model.Session;
import com.example.backend.repository.SessionRepo;

@RestController
@RequestMapping("/api/session")
@CrossOrigin(origins = "http://localhost:5173")
public class SessionController {

    @Autowired
    private SessionRepo sessionRepo;

    @PostMapping("/create")
    public String createSession(@RequestBody Session session) {

        sessionRepo.save(session);

        return "Session Created Successfully";
    }

    @GetMapping("/all")
    public List<Session> getAllSessions() {

        return sessionRepo.findAll();
    }

    @GetMapping("/{id}")
    public Session getSession(@PathVariable String id) {

        return sessionRepo.findById(id).orElse(null);
    }

    @DeleteMapping("/{id}")
    public String deleteSession(@PathVariable String id) {

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
            @PathVariable String id,
            @RequestBody Session session) {

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