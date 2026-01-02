package com.ipd.model;

public class Worker {
    private String email; // FK references User.email

    public Worker() {}

    public Worker(String email) {
        this.email = email;
    }

    // getters & setters
}
