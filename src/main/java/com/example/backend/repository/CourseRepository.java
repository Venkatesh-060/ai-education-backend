package com.example.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.backend.model.Course;

public interface CourseRepository extends MongoRepository<Course, String> {

    Page<Course> findByCourseNameContainingIgnoreCase(
            String search,
            Pageable pageable);

    Page<Course> findByCategoryIgnoreCase(
            String category,
            Pageable pageable);

    Page<Course> findByArchived(
            boolean archived,
            Pageable pageable);

    long countByArchived(boolean archived);

}