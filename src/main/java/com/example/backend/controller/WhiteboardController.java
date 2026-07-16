package com.example.backend.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.backend.dto.WhiteboardRequest;
import com.example.backend.model.Whiteboard;
import com.example.backend.repository.WhiteboardRepo;
import org.springframework.security.access.prepost.PreAuthorize;
import com.example.backend.repository.SessionRepo;

@RestController
@RequestMapping("/api/whiteboard")
@CrossOrigin(origins = "http://localhost:5173")
public class WhiteboardController {

    @Autowired
    WhiteboardRepo whiteboardRepo;

    @Autowired
    SessionRepo sessionRepo;

    @PostMapping("/save")
    public String save(@RequestBody WhiteboardRequest request) {

        if (request.getSessionId() == null || request.getSessionId().isBlank()) {
            return "Session Id Required";
        }

        if (request.getDrawingData() == null || request.getDrawingData().isBlank()) {
            return "Drawing Data Required";
        }

        Whiteboard board = whiteboardRepo
                .findFirstBySessionId(request.getSessionId())
                .orElse(new Whiteboard());

        board.setSessionId(request.getSessionId());
        board.setUserId(request.getUserId());
        board.setDrawingData(request.getDrawingData());
        board.setToolType(request.getToolType());
        board.setColor(request.getColor());
        board.setStrokeWidth(request.getStrokeWidth());
        board.setTimestamp(LocalDateTime.now());

        whiteboardRepo.save(board);

        return "Drawing Saved";
    }

    @GetMapping("/{sessionId}")
    public Whiteboard getBoard(@PathVariable String sessionId) {

        return whiteboardRepo
                .findFirstBySessionId(sessionId)
                .orElse(null);
    }

    @PutMapping("/{id}")
    public String update(@PathVariable String id,
            @RequestBody WhiteboardRequest request) {

        Whiteboard board = whiteboardRepo.findById(id).orElse(null);

        if (board == null)
            return "Not Found";

        board.setDrawingData(request.getDrawingData());
        board.setToolType(request.getToolType());
        board.setColor(request.getColor());
        board.setStrokeWidth(request.getStrokeWidth());
        board.setTimestamp(LocalDateTime.now());

        whiteboardRepo.save(board);

        return "Updated Successfully";
    }

    @DeleteMapping("/{sessionId}")
    public String clear(@PathVariable String sessionId) {

        Whiteboard board = whiteboardRepo
                .findFirstBySessionId(sessionId)
                .orElse(null);

        if (board == null) {
            return "Whiteboard Not Found";
        }

        board.setDrawingData("[]");
        board.setTimestamp(LocalDateTime.now());
        whiteboardRepo.save(board);
        return "Whiteboard Cleared";
    }

}