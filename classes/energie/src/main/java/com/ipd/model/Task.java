package com.ipd.model;

public class Task {
    private String description;
    private String status;
    private String createdAt;
    private String confirmedAt;
    private String achievedAt;
    private String clientEmail; // FK references Client.email
    private String workerEmail; // FK references Worker.email

    public Task() {}

    public Task(String description, String status, String createdAt, String confirmedAt, String achievedAt, String clientEmail, String workerEmail) {
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
        this.confirmedAt = confirmedAt;
        this.achievedAt = achievedAt;
        this.clientEmail = clientEmail;
        this.workerEmail = workerEmail;
    }

    // getters & setters
}
