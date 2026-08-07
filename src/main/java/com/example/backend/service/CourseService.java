package com.example.backend.service;

import org.springframework.data.domain.Page;

import com.example.backend.dto.CourseStatisticsDTO;
import com.example.backend.model.Course;

public interface CourseService {

    Course saveCourse(Course course);

    Page<Course> getCourses(
            int page,
            int size,
            String search,
            String category);

    Course getCourse(String id);

    Course updateCourse(String id, Course course);

    Course assignTrainer(String courseId, String trainerId);

    Course archiveCourse(String id);

    void deleteCourse(String id);

    CourseStatisticsDTO getStatistics();

}