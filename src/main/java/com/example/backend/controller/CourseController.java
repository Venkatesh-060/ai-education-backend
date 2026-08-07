package com.example.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import com.example.backend.repository.UserRepo;
import com.example.backend.dto.CourseStatisticsDTO;
import com.example.backend.model.Course;
import com.example.backend.model.User;
import com.example.backend.service.CourseService;

@RestController
@RequestMapping("/api/admin/courses")
@CrossOrigin(origins = "http://localhost:5173")
public class CourseController {

    @Autowired
    private CourseService service;

    @Autowired
    private UserRepo userRepo;

    @PostMapping
    public Course save(@RequestBody Course course) {

        return service.saveCourse(course);

    }

    @GetMapping
    public Page<Course> getCourses(

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "5") int size,

            @RequestParam(defaultValue = "") String search,

            @RequestParam(defaultValue = "") String category

    ) {

        return service.getCourses(
                page,
                size,
                search,
                category);

    }

    @GetMapping("/{id}")
    public Course getCourse(@PathVariable String id) {

        return service.getCourse(id);

    }

    @PutMapping("/{id}")
    public Course update(

            @PathVariable String id,

            @RequestBody Course course

    ) {

        return service.updateCourse(id, course);

    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {

        service.deleteCourse(id);

    }

    @PutMapping("/{courseId}/trainer/{trainerId}")
    public Course assignTrainer(

            @PathVariable String courseId,

            @PathVariable String trainerId) {

        return service.assignTrainer(courseId, trainerId);

    }

    @PutMapping("/{id}/archive")
    public Course archiveCourse(@PathVariable String id) {

        return service.archiveCourse(id);

    }

    @GetMapping("/statistics")
    public CourseStatisticsDTO statistics() {

        return service.getStatistics();

    }

    @GetMapping("/trainers")
    public List<User> trainers() {

        return userRepo.findByRole("TRAINER");

    }

}