package com.example.backend.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.backend.model.User;

public interface UserRepo extends MongoRepository<User, String> {

    User findByEmail(String email);

    List<User> findByRole(String role);

    Page<User> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
            String firstName,
            String lastName,
            String email,
            Pageable pageable);

    Page<User> findByRole(String role, Pageable pageable);
}