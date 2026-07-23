package com.example.backend.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.backend.model.Participant;
import com.example.backend.repository.ParticipantRepo;

@RestController
@RequestMapping("/api/participants")
@CrossOrigin(origins = "http://localhost:5173")
public class ParticipantController {

    @Autowired
    private ParticipantRepo participantRepo;

    // Join Session
    @PostMapping("/join")
    public String joinParticipant(@RequestBody Participant participant) {

        System.out.println("===== JOIN API HIT =====");

        List<Participant> participants = participantRepo.findBySessionIdAndUserId(
                participant.getSessionId(),
                participant.getUserId());

        if (!participants.isEmpty()) {

            Participant oldParticipant = participants.get(0);

            if (Boolean.FALSE.equals(oldParticipant.getCanRejoin())) {
                return "You are removed by trainer";
            }

            oldParticipant.setStatus("ACTIVE");
            oldParticipant.setJoinedAt(LocalDateTime.now());

            participantRepo.save(oldParticipant);

            return "Joined Successfully";
        }

        participant.setStatus("ACTIVE");
        participant.setCanRejoin(true);
        participant.setJoinedAt(LocalDateTime.now());

        participantRepo.save(participant);

        return "Joined Successfully";
    }

    // View Participants
    @GetMapping("/{sessionId}")
    public List<Participant> getParticipants(
            @PathVariable String sessionId) {

        return participantRepo.findBySessionId(sessionId);
    }

    // Remove Participant
    @PutMapping("/remove/{id}")
    public String removeParticipant(
            @PathVariable String id) {

        Participant participant = participantRepo.findById(id).orElse(null);

        if (participant == null) {
            return "Participant Not Found";
        }

        participant.setStatus("REMOVED");
        participant.setCanRejoin(false);

        participantRepo.save(participant);

        return "Participant Removed";
    }

    // Allow Rejoin
    @PutMapping("/allow/{id}")
    public String allowRejoin(
            @PathVariable String id) {

        Participant participant = participantRepo.findById(id).orElse(null);

        if (participant == null) {
            return "Participant Not Found";
        }

        participant.setCanRejoin(true);
        participant.setStatus("DISCONNECTED");

        participantRepo.save(participant);

        return "Participant Can Rejoin";
    }

    // Disconnect
    @PutMapping("/disconnect")
    public String disconnectParticipant(
            @RequestBody Participant participant) {

        List<Participant> participants = participantRepo.findBySessionIdAndUserId(
                participant.getSessionId(),
                participant.getUserId());

        if (participants.isEmpty()) {
            return "Participant Not Found";
        }

        Participant oldParticipant = participants.get(0);

        oldParticipant.setStatus("DISCONNECTED");
        oldParticipant.setLeftAt(LocalDateTime.now());

        participantRepo.save(oldParticipant);

        return "Disconnected";
    }
}