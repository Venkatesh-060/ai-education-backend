package com.example.backend.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="attendance")

public class Attendance {

    @Id
    private String id;

    private String userId;
    private String sessionId;

    private String joinTime;
    private String leaveTime;

    private long duration;

    private String status;

     private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Attendance() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id=id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId=userId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId=sessionId;
    }

    public String getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(String joinTime) {
        this.joinTime=joinTime;
    }

    public String getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(String leaveTime) {
        this.leaveTime=leaveTime;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration=duration;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status=status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }


    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt=createdAt;
    }



    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }


    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt=updatedAt;
    }

}