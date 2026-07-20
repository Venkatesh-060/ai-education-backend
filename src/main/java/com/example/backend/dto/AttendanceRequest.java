package com.example.backend.dto;
import jakarta.validation.constraints.NotBlank;

public class AttendanceRequest {

    @NotBlank
    private String userId;

    @NotBlank
    private String sessionId;

    private String joinTime;

    private String leaveTime;

    private String status;

    public AttendanceRequest() {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status=status;
    }

}