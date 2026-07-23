package com.example.backend.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.backend.model.RaiseHand;
import com.example.backend.repository.RaiseHandRepo;

@RestController
@RequestMapping("/api/raisehand")
@CrossOrigin(origins = "http://localhost:5173")
public class RaiseHandController {

    @Autowired
    RaiseHandRepo repo;

    @PostMapping("/raise")
    public String raiseHand(@RequestBody RaiseHand hand) {

        List<RaiseHand> old = repo.findByStudentIdAndSessionId(
                hand.getStudentId(),
                hand.getSessionId());

        if (!old.isEmpty()) {
            return "Already Raised";
        }

        hand.setStatus("PENDING");
        hand.setRequestedAt(LocalDateTime.now());

        repo.save(hand);

        return "Hand Raised";
    }

    @GetMapping("/{sessionId}")
    public List<RaiseHand> getHands(
            @PathVariable String sessionId) {

        return repo.findBySessionIdAndStatus(sessionId, "PENDING");

    }

    @PutMapping("/approve/{id}")
    public String approve(@PathVariable String id) {

        RaiseHand hand = repo.findById(id).orElse(null);

        if (hand == null) {
            return "Not Found";
        }

        hand.setStatus("APPROVED");
        repo.save(hand);

        return "Approved";
    }

    @PutMapping("/dismiss/{id}")
    public String dismiss(@PathVariable String id) {

        RaiseHand hand = repo.findById(id).orElse(null);

        if (hand == null) {
            return "Not Found";
        }

        hand.setStatus("DISMISSED");
        repo.save(hand);

        return "Dismissed";
    }

    @DeleteMapping("/{id}")
    public String lowerHand(
            @PathVariable String id) {

        repo.deleteById(id);

        return "Lowered";

    }

    @GetMapping("/student/{sessionId}/{studentId}")
    public RaiseHand getStudentHand(
            @PathVariable String sessionId,
            @PathVariable String studentId) {

        List<RaiseHand> list = repo.findByStudentIdAndSessionId(studentId, sessionId);

        if (list.isEmpty()) {
            return null;
        }

        return list.get(0);
    }

}