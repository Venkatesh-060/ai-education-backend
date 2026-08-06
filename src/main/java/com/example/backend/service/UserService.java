package com.example.backend.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.backend.model.User;
import com.example.backend.repository.UserRepo;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public Page<User> getUsers(int page,
            int size,
            String search,
            String role) {

        Pageable pageable = PageRequest.of(page, size);

        if (search != null && !search.isBlank()) {

            return userRepo
                    .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                            search,
                            search,
                            search,
                            pageable);
        }

        if (role != null && !role.isBlank()) {

            return userRepo.findByRole(role, pageable);
        }

        return userRepo.findAll(pageable);
    }

    public User createUser(User user) {

        user.setPassword(
                passwordEncoder.encode(user.getPassword()));

        return userRepo.save(user);
    }

    public User updateUser(String id,
            User updated) {

        Optional<User> optional = userRepo.findById(id);

        if (optional.isEmpty()) {

            throw new RuntimeException("User Not Found");
        }

        User user = optional.get();

        user.setFirstName(updated.getFirstName());
        user.setLastName(updated.getLastName());
        user.setEmail(updated.getEmail());
        user.setRole(updated.getRole());

        return userRepo.save(user);
    }

    public void deleteUser(String id) {

        userRepo.deleteById(id);
    }

    public void resetPassword(String id,
            String password) {

        User user = userRepo.findById(id).orElseThrow();

        user.setPassword(
                passwordEncoder.encode(password));

        userRepo.save(user);
    }

    public void changeRole(String id,
            String role) {

        User user = userRepo.findById(id).orElseThrow();

        user.setRole(role);

        userRepo.save(user);
    }

    public void toggleStatus(String id) {

    User user = userRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("User Not Found"));

    user.setActive(!user.isActive());

    userRepo.save(user);
}
}