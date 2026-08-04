package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RecentRegistrationDTO {

    private String name;
    private String email;
    private String role;
}