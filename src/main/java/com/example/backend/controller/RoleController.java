package com.example.backend.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoleController {

    @GetMapping("/admin/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public String admin() {

        return "Welcome Admin";

    }

    @GetMapping("/teacher/dashboard")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public String teacher() {

        return "Welcome Teacher";

    }

    @GetMapping("/student/dashboard")
    @PreAuthorize("hasAnyRole('ADMIN','STUDENT')")
    public String student() {

        return "Welcome Student";

    }

}