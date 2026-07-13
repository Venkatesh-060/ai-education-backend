package com.example.backend.dto;

public class LoginResponse {

    private String token;
    private String role;
    private String userId;


    public LoginResponse(String token,String role,String userId){

        this.token = token;
        this.role = role;
        this.userId = userId;
    }


    public String getToken(){
        return token;
    }

    public String getRole(){
        return role;
    }

    public String getUserId(){
        return userId;
    }


    public void setToken(String token){
        this.token = token;
    }

    public void setRole(String role){
        this.role = role;
    }

    public void setUserId(String userId){
        this.userId = userId;
    }

}