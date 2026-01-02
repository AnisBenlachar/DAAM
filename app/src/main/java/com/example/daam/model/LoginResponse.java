package com.example.daam.model;

public class LoginResponse {
    private String token;
    private UserDTO user;

    public String getToken() {
        return token;
    }

    public UserDTO getUser() {
        return user;
    }
}
