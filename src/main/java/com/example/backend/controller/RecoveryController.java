package com.example.backend.controller;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.backend.model.SessionRecovery;
import com.example.backend.repository.SessionRecoveryRepo;
import java.util.HashMap;
import java.util.Map;
import com.example.backend.repository.ChatRepo;
import com.example.backend.repository.ParticipantRepo;
import com.example.backend.repository.RaiseHandRepo;
import com.example.backend.repository.WhiteboardRepo;

@RestController
@RequestMapping("/api/recovery")
public class RecoveryController {

    @Autowired
    private SessionRecoveryRepo repo;

    @Autowired
    private ParticipantRepo participantRepo;

    @Autowired
    private ChatRepo chatRepo;

    @Autowired
    private RaiseHandRepo raiseHandRepo;

    @Autowired
    private WhiteboardRepo whiteboardRepo;

    @PostMapping("/disconnect")
    public String disconnect(@RequestBody SessionRecovery recovery) {

        SessionRecovery old = repo.findBySessionIdAndTrainerId(
                recovery.getSessionId(),
                recovery.getTrainerId())
                .orElse(new SessionRecovery());

        old.setSessionId(recovery.getSessionId());
        old.setTrainerId(recovery.getTrainerId());
        old.setConnected(false);
        old.setDisconnectedAt(LocalDateTime.now());
        old.setReason(recovery.getReason());

        repo.save(old);

        return "Trainer disconnected";
    }

    @PostMapping("/reconnect")
    public String reconnect(@RequestBody SessionRecovery recovery) {

        SessionRecovery old = repo.findBySessionIdAndTrainerId(
                recovery.getSessionId(),
                recovery.getTrainerId())
                .orElse(new SessionRecovery());

        old.setSessionId(recovery.getSessionId());
        old.setTrainerId(recovery.getTrainerId());
        old.setConnected(true);
        old.setReconnectedAt(LocalDateTime.now());

        repo.save(old);

        return "Trainer reconnected";
    }

    @GetMapping("/state/{sessionId}")
    public Map<String, Object> restoreSession(
            @PathVariable String sessionId) {

        Map<String, Object> state = new HashMap<>();

        state.put("participants",
                participantRepo.findBySessionId(sessionId));

        state.put("chat",
                chatRepo.findBySessionIdOrderByTimestampAsc(sessionId));

        state.put("hands",
                raiseHandRepo.findBySessionIdAndStatus(
                        sessionId,
                        "PENDING"));

        state.put("whiteboard",
                whiteboardRepo
                        .findFirstBySessionId(sessionId)
                        .orElse(null));

        return state;
    }

}
