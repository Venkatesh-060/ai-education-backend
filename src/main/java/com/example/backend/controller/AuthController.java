package com.example.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.example.backend.dto.LoginRequest;
import com.example.backend.dto.LoginResponse;
import com.example.backend.dto.ResetPasswordRequest;
import com.example.backend.jwt.JwtUtil;
import com.example.backend.model.User;
import com.example.backend.repository.UserRepo;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    @Autowired
    UserRepo userRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    // ================= REGISTER =================

    @PostMapping("/register")
    public String register(@RequestBody User user) {

        if (userRepo.findByEmail(user.getEmail()) != null) {
            return "Email Already Exists";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepo.save(user);

        return "Registration Successful";
    }

    // ================= LOGIN =================

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {

        User user = userRepo.findByEmail(request.getEmail());

        if (user == null) {
            throw new RuntimeException("User Not Found");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid Password");
        }

        String token = JwtUtil.generateToken(user.getEmail(), user.getRole());

        return new LoginResponse(token, user.getRole(),user.getId());
    }

    // ================= RESET PASSWORD =================

    @PostMapping("/reset-password")
    public String resetPassword(@RequestBody ResetPasswordRequest request) {

        User user = userRepo.findByEmail(request.getEmail());

        if (user == null) {
            return "User Not Found";
        }

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepo.save(user);

        return "Password Changed Successfully";
    }

    // ================= ENCODE OLD PASSWORD =================

    @GetMapping("/encode")
    public String encodePassword() {

        User user = userRepo.findByEmail("admin@gmail.com");

        if (user == null) {
            return "Admin Not Found";
        }

        user.setPassword(passwordEncoder.encode("Admin@123"));

        userRepo.save(user);

        return "Password Updated";
    }
}