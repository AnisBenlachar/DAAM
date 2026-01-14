package com.example.daam.model;

public class UserDTO {
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String role; // "WORKER" or "CLIENT"
    private Double rating;

    public UserDTO() {
    }

    public UserDTO(String email, String firstName, String lastName, String phoneNumber, String role, Double rating) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.rating = rating;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getRole() {
        return role;
    }

    public Double getRating() {
        return rating;
    }
}
