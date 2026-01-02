package com.ipd.model;

public class Seller {
    private String email; // FK references User.email

    public Seller() {}

    public Seller(String email) {
        this.email = email;
    }

    // getters & setters
}
