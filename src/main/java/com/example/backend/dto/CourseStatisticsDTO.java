package com.example.backend.dto;

public class CourseStatisticsDTO {

    private long totalCourses;

    private long activeCourses;

    private long archivedCourses;

    public long getTotalCourses() {
        return totalCourses;
    }

    public void setTotalCourses(long totalCourses) {
        this.totalCourses = totalCourses;
    }

    public long getActiveCourses() {
        return activeCourses;
    }

    public void setActiveCourses(long activeCourses) {
        this.activeCourses = activeCourses;
    }

    public long getArchivedCourses() {
        return archivedCourses;
    }

    public void setArchivedCourses(long archivedCourses) {
        this.archivedCourses = archivedCourses;
    }

}