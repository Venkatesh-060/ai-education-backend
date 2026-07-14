package com.example.backend.dto;

public class AttendanceStats {

    private int totalStudents;
    private int present;
    private int absent;
    private double attendancePercentage;

    public AttendanceStats() {
    }

    public AttendanceStats(int totalStudents,
            int present,
            int absent,
            double attendancePercentage) {

        this.totalStudents = totalStudents;
        this.present = present;
        this.absent = absent;
        this.attendancePercentage = attendancePercentage;
    }

    public int getTotalStudents() {
        return totalStudents;
    }

    public void setTotalStudents(int totalStudents) {
        this.totalStudents = totalStudents;
    }

    public int getPresent() {
        return present;
    }

    public void setPresent(int present) {
        this.present = present;
    }

    public int getAbsent() {
        return absent;
    }

    public void setAbsent(int absent) {
        this.absent = absent;
    }

    public double getAttendancePercentage() {
        return attendancePercentage;
    }

    public void setAttendancePercentage(double attendancePercentage) {
        this.attendancePercentage = attendancePercentage;
    }
}