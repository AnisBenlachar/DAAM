package com.ipd.model;

public class Client {
    private String email; // FK references User.email

    public Client() {}

    public Client(String email) {
        this.email = email;
    }

    // getters & setters
}
