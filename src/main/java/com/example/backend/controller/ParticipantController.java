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
            oldParticipant.setLeftAt(null);

            participantRepo.save(oldParticipant);

            return "Joined Successfully";
        }

        participant.setStatus("ACTIVE");
        participant.setCanRejoin(true);
        participant.setJoinedAt(LocalDateTime.now());
        participant.setLeftAt(null);

        participantRepo.save(participant);

        return "Joined Successfully";
    }

    @GetMapping("/{sessionId}")
    public List<Participant> getParticipants(
            @PathVariable String sessionId) {

        return participantRepo.findBySessionId(sessionId);
    }

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

        oldParticipant.setStatus("DISCONNECTED");
        oldParticipant.setLeftAt(leftTime);

        participantRepo.save(oldParticipant);

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