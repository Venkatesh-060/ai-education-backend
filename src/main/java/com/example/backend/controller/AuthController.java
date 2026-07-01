package com.example.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.backend.dto.ResetPasswordRequest;
import com.example.backend.model.User;
import com.example.backend.repository.UserRepo;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    UserRepo userRepo;

    @PostMapping("/reset-password")
    public String resetPassword(
            @RequestBody ResetPasswordRequest req) {

             System.out.println("Email: " + req.getEmail());
             System.out.println("New Password: " + req.getPassword());

        User user = userRepo.findByEmail(req.getEmail());

        if (user == null) {
            return "User Not Found";
        }

        user.setPassword(req.getPassword());

        userRepo.save(user);

        User updatedUser = userRepo.save(user);
        System.out.println("Saved Password: " + updatedUser.getPassword());

        return "Password Changed Successfully";
    }
}