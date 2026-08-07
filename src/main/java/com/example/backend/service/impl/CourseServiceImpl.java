package com.example.backend.service.impl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.backend.dto.CourseStatisticsDTO;
import com.example.backend.model.Course;
import com.example.backend.model.User;
import com.example.backend.repository.CourseRepository;
import com.example.backend.repository.UserRepo;
import com.example.backend.service.CourseService;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository repository;

    @Autowired
    private UserRepo userRepo;

    // ==========================
    // Create Course
    // ==========================

    @Override
    public Course saveCourse(Course course) {

        course.setArchived(false);
        course.setCreatedAt(LocalDateTime.now());
        course.setUpdatedAt(LocalDateTime.now());

        return repository.save(course);
    }

    // ==========================
    // Get All Courses
    // ==========================

    @Override
    public Page<Course> getCourses(
            int page,
            int size,
            String search,
            String category) {

        Pageable pageable = PageRequest.of(page, size);

        if (search != null && !search.isBlank()) {
            return repository.findByCourseNameContainingIgnoreCase(
                    search,
                    pageable);
        }

        if (category != null && !category.isBlank()) {
            return repository.findByCategoryIgnoreCase(
                    category,
                    pageable);
        }

        return repository.findAll(pageable);
    }

    // ==========================
    // Get Single Course
    // ==========================

    @Override
    public Course getCourse(String id) {

        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course Not Found"));
    }

    // ==========================
    // Update Course
    // ==========================

    @Override
    public Course updateCourse(String id, Course course) {

        Course existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course Not Found"));

        existing.setCourseName(course.getCourseName());
        existing.setDescription(course.getDescription());
        existing.setCategory(course.getCategory());
        existing.setDuration(course.getDuration());

        existing.setUpdatedAt(LocalDateTime.now());

        return repository.save(existing);
    }

    // ==========================
    // Assign Trainer
    // ==========================

    @Override
    public Course assignTrainer(String courseId, String trainerId) {

        Course course = repository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course Not Found"));

        User trainer = userRepo.findById(trainerId)
                .orElseThrow(() -> new RuntimeException("Trainer Not Found"));

        if (!"TRAINER".equalsIgnoreCase(trainer.getRole())) {
            throw new RuntimeException("Selected user is not a Trainer");
        }

        course.setTrainerId(trainer.getId());
        course.setTrainerName(
                trainer.getFirstName() + " " + trainer.getLastName());

        course.setUpdatedAt(LocalDateTime.now());

        return repository.save(course);
    }

    // ==========================
    // Archive Course
    // ==========================

    @Override
    public Course archiveCourse(String id) {

        Course course = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course Not Found"));

        course.setArchived(true);
        course.setUpdatedAt(LocalDateTime.now());

        return repository.save(course);
    }

    // ==========================
    // Delete Course
    // ==========================

    @Override
    public void deleteCourse(String id) {

        repository.deleteById(id);
    }

    // ==========================
    // Statistics
    // ==========================

    @Override
    public CourseStatisticsDTO getStatistics() {

        CourseStatisticsDTO dto = new CourseStatisticsDTO();

        dto.setTotalCourses(repository.count());
        dto.setActiveCourses(repository.countByArchived(false));
        dto.setArchivedCourses(repository.countByArchived(true));

        return dto;
    }

}