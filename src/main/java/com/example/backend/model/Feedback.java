package com.example.backend.model;

import java.time.LocalDateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "feedback")
public class Feedback {

    @Id
    private String id;
    private String sessionId;
    private String sessionName;
    private String studentId;
    private String studentName;
    private String trainerId;
    private String trainerName;
    private Integer rating;
    private String review;
    private String tags;
    private LocalDateTime createdAt;

    public Feedback() {
    }

    public Feedback(String sessionId,
            String sessionName,
            String studentId,
            String studentName,
            String trainerId,
            String trainerName,
            Integer rating,
            String review,
            String tags,
            LocalDateTime createdAt) {

        this.sessionId = sessionId;
        this.sessionName = sessionName;
        this.studentId = studentId;
        this.studentName = studentName;
        this.trainerId = trainerId;
        this.trainerName = trainerName;
        this.rating = rating;
        this.review = review;
        this.tags = tags;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(String trainerId) {
        this.trainerId = trainerId;
    }

    public String getTrainerName() {
        return trainerName;
    }

    public void setTrainerName(String trainerName) {
        this.trainerName = trainerName;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}