package com.example.backend.controller;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.backend.model.Attendance;
import com.example.backend.model.Participant;
import com.example.backend.repository.AttendanceRepo;
import com.example.backend.repository.ParticipantRepo;

@RestController
@RequestMapping("/api/participants")
@CrossOrigin(origins = "http://localhost:5173")
public class ParticipantController {

    @Autowired
    private ParticipantRepo participantRepo;

    @Autowired
    private AttendanceRepo attendanceRepo;

    // =========================================================
    // JOIN SESSION
    // =========================================================

    @PostMapping("/join")
    public String joinParticipant(@RequestBody Participant participant) {

        System.out.println("===== JOIN API HIT =====");

        List<Participant> participants = participantRepo.findBySessionIdAndUserId(
                participant.getSessionId(),
                participant.getUserId());

        // Existing participant
        if (!participants.isEmpty()) {

            Participant oldParticipant = participants.get(0);

            // Trainer removed the participant
            if (Boolean.FALSE.equals(oldParticipant.getCanRejoin())) {
                return "You are removed by trainer";
            }

            oldParticipant.setStatus("ACTIVE");
            oldParticipant.setJoinedAt(LocalDateTime.now());
            oldParticipant.setLeftAt(null);

            participantRepo.save(oldParticipant);

            return "Joined Successfully";
        }

        // New participant
        participant.setStatus("ACTIVE");
        participant.setCanRejoin(true);
        participant.setJoinedAt(LocalDateTime.now());
        participant.setLeftAt(null);

        participantRepo.save(participant);

        return "Joined Successfully";
    }

    // =========================================================
    // GET PARTICIPANTS
    // =========================================================

    @GetMapping("/{sessionId}")
    public List<Participant> getParticipants(
            @PathVariable String sessionId) {

        return participantRepo.findBySessionId(sessionId);
    }

    // =========================================================
    // REMOVE PARTICIPANT
    // =========================================================

    @PutMapping("/remove/{id}")
    public String removeParticipant(
            @PathVariable String id) {

        Participant participant = participantRepo.findById(id).orElse(null);

        if (participant == null) {
            return "Participant Not Found";
        }

        participant.setStatus("REMOVED");
        participant.setCanRejoin(false);
        participant.setLeftAt(LocalDateTime.now());

        participantRepo.save(participant);

        return "Participant Removed";
    }

    // =========================================================
    // ALLOW REJOIN
    // =========================================================

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

    // =========================================================
    // DISCONNECT PARTICIPANT
    // =========================================================

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

        LocalDateTime leftTime = LocalDateTime.now();

        // Update participant
        oldParticipant.setStatus("DISCONNECTED");
        oldParticipant.setLeftAt(leftTime);

        participantRepo.save(oldParticipant);

        // =====================================================
        // UPDATE ATTENDANCE
        // =====================================================

        List<Attendance> attendanceList = attendanceRepo.findByUserIdAndSessionId(
                participant.getUserId(),
                participant.getSessionId());

        if (!attendanceList.isEmpty()) {

            Attendance attendance = attendanceList.get(0);

            if (attendance.getJoinTime() != null) {

                try {

                    LocalDateTime joinTime = LocalDateTime.parse(
                            attendance.getJoinTime());

                    long minutes = Duration.between(
                            joinTime,
                            leftTime)
                            .toMinutes();

                    attendance.setLeaveTime(
                            leftTime.toString());

                    attendance.setDuration(minutes);

                    attendance.setUpdatedAt(
                            LocalDateTime.now());

                    // Mark as Left Early if less than 30 minutes
                    if (minutes < 30) {
                        attendance.setStatus("Left Early");
                    }

                    attendanceRepo.save(attendance);

                } catch (Exception e) {

                    System.out.println(
                            "Attendance time parsing error: "
                                    + e.getMessage());
                }
            }
        }

        return "Disconnected";
    }
}