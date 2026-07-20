package com.example.backend.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.backend.model.Attendance;

public interface AttendanceRepo extends MongoRepository<Attendance, String> {

    List<Attendance> findBySessionId(String sessionId);

    List<Attendance> findByUserId(String userId);

    // Return List instead of Attendance
    List<Attendance> findByUserIdAndSessionId(String userId, String sessionId);

}