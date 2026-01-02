 package com.ipd.model;

public class Admin {
    private String email; // FK references User.email
    private String permissions;

    public Admin() {}

    public Admin(String email, String permissions) {
        this.email = email;
        this.permissions = permissions;
    }

    // getters & setters
}
