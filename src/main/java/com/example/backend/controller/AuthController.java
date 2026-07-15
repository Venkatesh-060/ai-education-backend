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
import com.example.backend.dto.RegisterRequest;
import com.example.backend.dto.RegisterResponse;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    @Autowired
    UserRepo userRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

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

        return new LoginResponse(token, user.getRole(), user.getId(), user.getFirstName(),
                user.getLastName());
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

    @PostMapping("/register")
    public RegisterResponse register(
            @RequestBody RegisterRequest request) {

        User oldUser = userRepo.findByEmail(request.getEmail());

        if (oldUser != null) {
            return new RegisterResponse("Email Already Exists");
        }

        User user = new User();

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        userRepo.save(user);

        return new RegisterResponse("Registration Successful");
    }
}