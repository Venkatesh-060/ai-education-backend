package com.example.backend.controller;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.example.backend.model.Session;

@RestController
@RequestMapping("/api/attendance")
@CrossOrigin(origins = "http://localhost:5173")

public class AttendanceController {

        @Autowired
        AttendanceRepo attendanceRepo;

        @Autowired
        UserRepo userRepo;

        @Autowired
        SessionRepo sessionRepo;

        // MARK ATTENDANCE

       @PostMapping("/mark")
public String markAttendance(@Valid @RequestBody AttendanceRequest request) {

    List<Attendance> oldAttendance =
            attendanceRepo.findByUserIdAndSessionId(
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
    
    Session session =
            sessionRepo.findById(request.getSessionId()).orElse(null);

    if (session != null) {

        LocalDateTime join =
                LocalDateTime.parse(request.getJoinTime());

        LocalDateTime start =
                LocalDateTime.parse(
                        session.getSessionDate() +
                        "T" +
                        session.getStartTime());

        if (join.isAfter(start.plusMinutes(10))) {

            attendance.setStatus("Late");

        } else {

            attendance.setStatus("Present");

        }

    } else {

        attendance.setStatus("Present");

    }

    attendance.setCreatedAt(LocalDateTime.now());
    attendance.setUpdatedAt(LocalDateTime.now());

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
public String updateAttendance(@Valid @RequestBody AttendanceRequest request) {

    System.out.println("UPDATE API CALLED");

    List<Attendance> attendanceList =
            attendanceRepo.findByUserIdAndSessionId(
                    request.getUserId(),
                    request.getSessionId());

    if (attendanceList.isEmpty()) {
        return "Attendance Not Found";
    }

    Attendance attendance = attendanceList.get(0);

    attendance.setLeaveTime(request.getLeaveTime());

    LocalDateTime join =
            LocalDateTime.parse(attendance.getJoinTime());

    LocalDateTime leave =
            LocalDateTime.parse(request.getLeaveTime());

    long minutes =
            Duration.between(join, leave).toMinutes();

    attendance.setDuration(minutes);

    // Don't overwrite Late status
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

        @GetMapping("/report")
        public List<AttendanceResponse> attendanceReport() {

                System.out.println("===== REPORT API CALLED =====");

                List<Attendance> attendanceList = attendanceRepo.findAll();

                System.out.println("Attendance Count = " + attendanceList.size());

                List<AttendanceResponse> report = new java.util.ArrayList<>();

                for (Attendance attendance : attendanceList) {

                        System.out.println(attendance.getId());

                        User user = userRepo.findById(attendance.getUserId()).orElse(null);

                        Session session = sessionRepo.findById(attendance.getSessionId()).orElse(null);

                        AttendanceResponse response = new AttendanceResponse();

                        response.setAttendanceId(attendance.getId());

                        response.setStudentId(attendance.getUserId());

                        response.setStudentName(
                                        user == null
                                                        ? "Unknown Student"
                                                        : user.getFirstName() + " " + user.getLastName());

                        response.setSessionId(attendance.getSessionId());

                        response.setSessionName(
                                        session == null
                                                        ? "Unknown Session"
                                                        : session.getSessionName());

                        response.setBatchName(
                                        session == null
                                                        ? "-"
                                                        : session.getBatchName());

                        response.setJoinTime(attendance.getJoinTime());

                        response.setLeaveTime(attendance.getLeaveTime());

                        response.setDuration(attendance.getDuration());

                        response.setStatus(attendance.getStatus());

                        report.add(response);
                }

                return report;
        }

}