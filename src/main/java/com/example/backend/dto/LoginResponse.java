package com.example.backend.dto;

public class LoginResponse {

    private String token;
    private String role;
    private String userId;
    private String firstName;
    private String lastName;

    public LoginResponse(String token, String role, String userId, String firstName,
            String lastName) {

        this.token = token;
        this.role = role;
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getToken() {
        return token;
    }

    public String getRole() {
        return role;
    }

    public String getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

}