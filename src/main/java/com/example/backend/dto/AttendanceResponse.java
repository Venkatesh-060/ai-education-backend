package com.example.backend.dto;

public class AttendanceResponse {

    private String attendanceId;
    private String studentId;
    private String studentName;

    private String sessionId;
    private String sessionName;

    private String batchName;

    private String joinTime;
    private String leaveTime;

    private long duration;

    private String status;

    public AttendanceResponse() {
    }

    public AttendanceResponse(
            String attendanceId,
            String studentId,
            String studentName,
            String sessionId,
            String sessionName,
            String batchName,
            String joinTime,
            String leaveTime,
            long duration,
            String status) {

        this.attendanceId = attendanceId;
        this.studentId = studentId;
        this.studentName = studentName;
        this.sessionId = sessionId;
        this.sessionName = sessionName;
        this.batchName = batchName;
        this.joinTime = joinTime;
        this.leaveTime = leaveTime;
        this.duration = duration;
        this.status = status;
    }

    public String getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(String attendanceId) {
        this.attendanceId = attendanceId;
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

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public String getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(String joinTime) {
        this.joinTime = joinTime;
    }

    public String getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(String leaveTime) {
        this.leaveTime = leaveTime;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}