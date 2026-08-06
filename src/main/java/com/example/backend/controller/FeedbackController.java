package com.example.backend.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.example.backend.dto.FeedbackRequest;
import com.example.backend.dto.FeedbackStats;
import com.example.backend.model.Feedback;
import com.example.backend.model.Participant;
import com.example.backend.model.Session;
import com.example.backend.repository.FeedbackRepository;
import com.example.backend.repository.ParticipantRepo;
import com.example.backend.repository.SessionRepo;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/feedback")
@Validated
public class FeedbackController {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private SessionRepo sessionRepository;

    @Autowired
    private ParticipantRepo participantRepository;

    @PostMapping
    public ResponseEntity<?> submitFeedback(
            @Valid @RequestBody FeedbackRequest request) {

        // Prevent duplicate feedback
        if (feedbackRepository.existsBySessionIdAndStudentId(
                request.getSessionId(),
                request.getStudentId())) {

            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Feedback already submitted for this session.");
        }

        Optional<Session> sessionOpt = sessionRepository.findById(request.getSessionId());

        if (sessionOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Session not found.");
        }

        Session session = sessionOpt.get();

        List<Participant> participants = participantRepository.findBySessionIdAndUserId(
                request.getSessionId(),
                request.getStudentId());

        if (participants.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Student is not a participant of this session.");
        }

        Participant student = participants.get(0);

        Feedback feedback = new Feedback();

        feedback.setSessionId(session.getId());
        feedback.setSessionName(session.getSessionName());
        feedback.setStudentId(student.getUserId());
        feedback.setStudentName(student.getName());
        feedback.setTrainerId(session.getTrainerId());
        feedback.setRating(request.getRating());
        feedback.setReview(request.getReview());
        feedback.setTags(request.getTags());
        feedback.setCreatedAt(LocalDateTime.now());
        feedbackRepository.save(feedback);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(feedback);
    }

    @GetMapping("/session/{sessionId}")
    public ResponseEntity<List<Feedback>> getFeedbackBySession(
            @PathVariable String sessionId) {

        List<Feedback> feedbackList = feedbackRepository.findBySessionId(sessionId);

        return ResponseEntity.ok(feedbackList);
    }

    @GetMapping("/trainer/{trainerId}")
    public ResponseEntity<List<Feedback>> getFeedbackByTrainer(
            @PathVariable String trainerId) {

        List<Feedback> feedbackList = feedbackRepository.findByTrainerId(trainerId);

        return ResponseEntity.ok(feedbackList);
    }

    @GetMapping("/average/{trainerId}")
    public ResponseEntity<Double> getAverageRating(
            @PathVariable String trainerId) {

        List<Feedback> feedbackList = feedbackRepository.findByTrainerId(trainerId);

        if (feedbackList.isEmpty()) {
            return ResponseEntity.ok(0.0);
        }

        double total = 0;

        for (Feedback feedback : feedbackList) {
            total += feedback.getRating();
        }

        double average = total / feedbackList.size();

        return ResponseEntity.ok(average);
    }

    @GetMapping("/distribution/{trainerId}")
    public ResponseEntity<FeedbackStats> getRatingDistribution(
            @PathVariable String trainerId) {

        List<Feedback> feedbackList = feedbackRepository.findByTrainerId(trainerId);

        FeedbackStats stats = new FeedbackStats();

        stats.setTotalFeedback(feedbackList.size());

        long one = 0;
        long two = 0;
        long three = 0;
        long four = 0;
        long five = 0;

        double total = 0;

        for (Feedback feedback : feedbackList) {

            total += feedback.getRating();

            switch (feedback.getRating()) {

                case 1:
                    one++;
                    break;

                case 2:
                    two++;
                    break;

                case 3:
                    three++;
                    break;

                case 4:
                    four++;
                    break;

                case 5:
                    five++;
                    break;
            }
        }

        stats.setOneStar(one);
        stats.setTwoStar(two);
        stats.setThreeStar(three);
        stats.setFourStar(four);
        stats.setFiveStar(five);

        if (!feedbackList.isEmpty()) {
            stats.setAverageRating(total / feedbackList.size());
        }

        return ResponseEntity.ok(stats);
    }

    @GetMapping("/all")
    public ResponseEntity<Page<Feedback>> getAllFeedback(

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "5") int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Feedback> feedbackPage = feedbackRepository.findAll(pageable);

        return ResponseEntity.ok(feedbackPage);
    }

}
