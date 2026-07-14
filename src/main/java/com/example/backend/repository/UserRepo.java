package com.example.backend.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.backend.model.User;

public interface UserRepo extends MongoRepository<User, String> {

    User findByEmail(String email);

    List<User> findByRole(String role);
}