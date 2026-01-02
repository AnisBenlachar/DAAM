package com.ipd.model;

public class User {
    private String firstName;
    private String lastName;
    private String email; // primary key
    private String password;
    private String birthDate;
    private String phoneNumber;

    public User() {}

    public User(String firstName, String lastName, String email, String password, String birthDate, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
    }

    // getters & setters
}
