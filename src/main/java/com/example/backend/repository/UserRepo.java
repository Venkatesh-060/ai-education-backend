package com.example.backend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.backend.model.User;

public interface UserRepo extends MongoRepository<User, String> {

    User findByEmail(String email);

}