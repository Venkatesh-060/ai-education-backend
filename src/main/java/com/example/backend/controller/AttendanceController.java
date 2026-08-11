package com.example.backend.controller;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import com.example.backend.dto.AttendanceRequest;
import com.example.backend.dto.AttendanceResponse;
import com.example.backend.model.Attendance;
import com.example.backend.repository.AttendanceRepo;
import com.example.backend.repository.SessionRepo;
import com.example.backend.repository.UserRepo;
import com.example.backend.model.User;
import com.example.backend.dto.AttendanceStats;

@RestController
@RequestMapping("/api/attendance")
@CrossOrigin(origins = "http://localhost:5173")

public class AttendanceController {

        private final AttendanceRepo attendanceRepo;
        private final UserRepo userRepo;
        private final SessionRepo sessionRepo;

        public AttendanceController(
                        AttendanceRepo attendanceRepo,
                        UserRepo userRepo,
                        SessionRepo sessionRepo) {

                this.attendanceRepo = attendanceRepo;
                this.userRepo = userRepo;
                this.sessionRepo = sessionRepo;
        }

        @PostMapping("/mark")
        public String markAttendance(@Valid @RequestBody AttendanceRequest request) {

                List<Attendance> oldAttendance = attendanceRepo.findByUserIdAndSessionId(
                                request.getUserId(),
                                request.getSessionId());

                if (!oldAttendance.isEmpty()) {
                        return "Attendance Already Marked";
                }

                Attendance attendance = new Attendance();
                attendance.setUserId(request.getUserId());
                attendance.setSessionId(request.getSessionId());
                attendance.setJoinTime(request.getJoinTime());
                attendance.setLeaveTime(request.getLeaveTime());
                sessionRepo.findById(request.getSessionId()).ifPresentOrElse(session -> {

                        LocalDateTime join = LocalDateTime.parse(request.getJoinTime());
                        LocalDateTime start = LocalDateTime.parse(
                                        session.getSessionDate() + "T" + session.getStartTime());

                        if (join.isAfter(start.plusMinutes(10))) {
                                attendance.setStatus("Late");
                        } else {
                                attendance.setStatus("Present");
                        }

                }, () -> attendance.setStatus("Present"));

                attendance.setCreatedAt(LocalDateTime.now());
                attendance.setUpdatedAt(LocalDateTime.now());
                attendanceRepo.save(attendance);
                return "Attendance Marked Successfully";
        }

        @GetMapping("/session/{sessionId}")

        public List<Attendance> getSessionAttendance(
                        @PathVariable String sessionId) {

                return attendanceRepo.findBySessionId(sessionId);

        }

        @GetMapping("/student/{userId}")

        public List<Attendance> getStudentAttendance(
                        @PathVariable String userId) {
                return attendanceRepo.findByUserId(userId);

        }

        @PutMapping("/update")
        public String updateAttendance(@Valid @RequestBody AttendanceRequest request) {

                List<Attendance> attendanceList = attendanceRepo.findByUserIdAndSessionId(
                                request.getUserId(),
                                request.getSessionId());

                if (attendanceList.isEmpty()) {
                        return "Attendance Not Found";
                }

                Attendance attendance = attendanceList.get(0);
                attendance.setLeaveTime(request.getLeaveTime());
                LocalDateTime join = LocalDateTime.parse(attendance.getJoinTime());
                LocalDateTime leave = LocalDateTime.parse(request.getLeaveTime());
                long minutes = Duration.between(join, leave).toMinutes();
                attendance.setDuration(minutes);

                if (minutes < 30) {

                        attendance.setStatus("Left Early");

                }

                attendance.setUpdatedAt(LocalDateTime.now());
                attendanceRepo.save(attendance);
                return "Attendance Updated";
        }

        @GetMapping("/all")
        public List<Attendance> getAllAttendance() {
                return attendanceRepo.findAll();
        }

        @GetMapping("/stats/{sessionId}")
        public AttendanceStats getStats(
                        @PathVariable String sessionId) {

                // Get all students
                List<User> students = userRepo.findByRole("STUDENT");

                // Get attendance records only for this session
                List<Attendance> attendanceList = attendanceRepo.findBySessionId(sessionId);

                int totalStudents = students.size();
                int present = 0;
                int absent = 0;

                // Count Present and Late
                for (Attendance attendance : attendanceList) {

                        if ("Present".equalsIgnoreCase(
                                        attendance.getStatus())) {

                                present++;

                        } else if ("Late".equalsIgnoreCase(
                                        attendance.getStatus())) {

                                // Late students are considered attended
                                present++;
                        }
                }

                // Calculate absent students
                absent = totalStudents - present;

                // Prevent negative value
                if (absent < 0) {
                        absent = 0;
                }

                // Calculate attendance percentage
                double percentage = 0.0;

                if (totalStudents > 0) {

                        percentage = ((double) present / totalStudents) * 100;
                }

                // Round to 2 decimal places
                percentage = Math.round(percentage * 100.0) / 100.0;

                return new AttendanceStats(
                                totalStudents,
                                present,
                                absent,
                                percentage);
        }

        @GetMapping("/report")
        public List<AttendanceResponse> attendanceReport() {

                List<Attendance> attendanceList = attendanceRepo.findAll();
                List<AttendanceResponse> report = new java.util.ArrayList<>();

                for (Attendance attendance : attendanceList) {
                        AttendanceResponse response = new AttendanceResponse();
                        response.setAttendanceId(attendance.getId());
                        response.setStudentId(attendance.getUserId());
                        response.setSessionId(attendance.getSessionId());
                        userRepo.findById(attendance.getUserId()).ifPresentOrElse(
                                        user -> response.setStudentName(
                                                        user.getFirstName() + " " + user.getLastName()),
                                        () -> response.setStudentName("Unknown Student"));

                        sessionRepo.findById(attendance.getSessionId()).ifPresentOrElse(
                                        session -> {
                                                response.setSessionName(session.getSessionName());
                                                response.setBatchName(session.getBatchName());
                                        },
                                        () -> {
                                                response.setSessionName("Unknown Session");
                                                response.setBatchName("-");
                                        });

                        response.setJoinTime(attendance.getJoinTime());
                        response.setLeaveTime(attendance.getLeaveTime());
                        response.setDuration(attendance.getDuration());
                        response.setStatus(attendance.getStatus());
                        report.add(response);
                }

                return report;
        }

}