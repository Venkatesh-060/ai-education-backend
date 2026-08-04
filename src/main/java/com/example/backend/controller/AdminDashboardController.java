package com.example.backend.controller;

import org.springframework.web.bind.annotation.*;

import com.example.backend.dto.AdminDashboardResponse;
import com.example.backend.service.AdminDashboardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/dashboard")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AdminDashboardController {

    private final AdminDashboardService service;

    @GetMapping
    public AdminDashboardResponse dashboard() {
        return service.getDashboard();
    }
}