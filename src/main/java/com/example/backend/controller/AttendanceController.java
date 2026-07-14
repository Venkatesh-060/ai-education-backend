package com.example.backend.controller;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import com.example.backend.dto.AttendanceRequest;
import com.example.backend.model.Attendance;
import com.example.backend.repository.AttendanceRepo;
import com.example.backend.repository.UserRepo;
import com.example.backend.model.User;
import com.example.backend.dto.AttendanceStats;
@RestController
@RequestMapping("/api/attendance")
@CrossOrigin(origins = "http://localhost:5173")

public class AttendanceController {

        @Autowired
        AttendanceRepo attendanceRepo;

        @Autowired
        UserRepo userRepo;


        // MARK ATTENDANCE

        @PostMapping("/mark")
        public String markAttendance(
                        @Valid @RequestBody AttendanceRequest request) {

                Attendance oldAttendance = attendanceRepo.findByUserIdAndSessionId(
                                request.getUserId(),
                                request.getSessionId());

                if (oldAttendance != null) {

                        return "Attendance Already Marked";

                }

                Attendance attendance = new Attendance();

                attendance.setUserId(request.getUserId());

                attendance.setSessionId(request.getSessionId());

                attendance.setJoinTime(request.getJoinTime());

                attendance.setLeaveTime(request.getLeaveTime());

                attendance.setStatus(request.getStatus());

                if (request.getJoinTime() != null &&
                                request.getLeaveTime() != null) {

                        LocalDateTime join = LocalDateTime.parse(
                                        request.getJoinTime());

                        LocalDateTime leave = LocalDateTime.parse(
                                        request.getLeaveTime());

                        attendance.setDuration(
                                        Duration.between(join, leave)
                                                        .toMinutes());

                }

                attendance.setCreatedAt(
                                LocalDateTime.now());

                attendance.setUpdatedAt(
                                LocalDateTime.now());

                attendanceRepo.save(attendance);

                return "Attendance Marked Successfully";

        }

        // GET SESSION ATTENDANCE

        @GetMapping("/session/{sessionId}")

        public List<Attendance> getSessionAttendance(
                        @PathVariable String sessionId) {

                return attendanceRepo.findBySessionId(sessionId);

        }

        // GET STUDENT ATTENDANCE

        @GetMapping("/student/{userId}")

        public List<Attendance> getStudentAttendance(
                        @PathVariable String userId) {

                return attendanceRepo.findByUserId(userId);

        }

        // UPDATE ATTENDANCE

        @PutMapping("/update")

        public String updateAttendance(
                        @Valid @RequestBody AttendanceRequest request) {

                Attendance attendance = attendanceRepo.findByUserIdAndSessionId(
                                request.getUserId(),
                                request.getSessionId());

                if (attendance == null) {

                        return "Attendance Not Found";

                }

                attendance.setLeaveTime(
                                request.getLeaveTime());

                attendance.setStatus(
                                request.getStatus());

                LocalDateTime join = LocalDateTime.parse(
                                attendance.getJoinTime());

                LocalDateTime leave = LocalDateTime.parse(
                                request.getLeaveTime());

                attendance.setDuration(
                                Duration.between(join, leave)
                                                .toMinutes());

                attendance.setUpdatedAt(
                                LocalDateTime.now());

                attendanceRepo.save(attendance);

                return "Attendance Updated";

        }

        @GetMapping("/all")
        public List<Attendance> getAllAttendance() {
                return attendanceRepo.findAll();
        }

        @GetMapping("/stats")
        public AttendanceStats getStats() {

                List<User> students = userRepo.findByRole("STUDENT");

                List<Attendance> attendanceList = attendanceRepo.findAll();

                int totalStudents = students.size();

                int present = 0;

                int absent = 0;

                for (Attendance attendance : attendanceList) {

                        if (attendance.getStatus().equalsIgnoreCase("Present")) {
                                present++;
                        } else {
                                absent++;
                        }

                }

                double percentage = 0;

                if (totalStudents > 0) {
                        percentage = ((double) present / totalStudents) * 100;
                }

                return new AttendanceStats(
                                totalStudents,
                                present,
                                absent,
                                percentage);

        }

}