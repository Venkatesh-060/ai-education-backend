package com.example.backend.dto;

public class SessionStatisticsResponse {

    private String sessionId;
    private String sessionName;
    private String status;

    private long durationMinutes;

    private long totalParticipants;
    private long activeParticipants;
    private long disconnectedParticipants;

    private long present;
    private long late;
    private long leftEarly;
    private long absent;

    private double attendancePercentage;

    public SessionStatisticsResponse() {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(long durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public long getTotalParticipants() {
        return totalParticipants;
    }

    public void setTotalParticipants(long totalParticipants) {
        this.totalParticipants = totalParticipants;
    }

    public long getActiveParticipants() {
        return activeParticipants;
    }

    public void setActiveParticipants(long activeParticipants) {
        this.activeParticipants = activeParticipants;
    }

    public long getDisconnectedParticipants() {
        return disconnectedParticipants;
    }

    public void setDisconnectedParticipants(
            long disconnectedParticipants) {

        this.disconnectedParticipants = disconnectedParticipants;
    }

    public long getPresent() {
        return present;
    }

    public void setPresent(long present) {
        this.present = present;
    }

    public long getLate() {
        return late;
    }

    public void setLate(long late) {
        this.late = late;
    }

    public long getLeftEarly() {
        return leftEarly;
    }

    public void setLeftEarly(long leftEarly) {
        this.leftEarly = leftEarly;
    }

    public long getAbsent() {
        return absent;
    }

    public void setAbsent(long absent) {
        this.absent = absent;
    }

    public double getAttendancePercentage() {
        return attendancePercentage;
    }

    public void setAttendancePercentage(
            double attendancePercentage) {

        this.attendancePercentage = attendancePercentage;
    }
}