package com.example.backend.controller;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import com.example.backend.model.User;
import com.example.backend.service.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    private final UserService userService;

    @GetMapping
    public Page<User> getUsers(

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "5") int size,

            @RequestParam(defaultValue = "") String search,

            @RequestParam(defaultValue = "") String role) {

        return userService.getUsers(page, size, search, role);
    }

    @PostMapping
    public User createUser(@RequestBody User user) {

        return userService.createUser(user);
    }

    @PutMapping("/{id}")
    public User updateUser(

            @PathVariable String id,

            @RequestBody User user) {

        return userService.updateUser(id, user);
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable String id) {

        userService.deleteUser(id);

        return "User Deleted Successfully";
    }

    @PutMapping("/{id}/password")
    public String resetPassword(

            @PathVariable String id,

            @RequestParam String password) {

        userService.resetPassword(id, password);

        return "Password Reset Successfully";
    }

    @PutMapping("/{id}/role")
    public String changeRole(

            @PathVariable String id,

            @RequestParam String role) {

        userService.changeRole(id, role);

        return "Role Updated Successfully";
    }

    @PutMapping("/{id}/status")
    public String toggleStatus(@PathVariable String id) {

        userService.toggleStatus(id);

        return "User Status Updated Successfully";
    }

}