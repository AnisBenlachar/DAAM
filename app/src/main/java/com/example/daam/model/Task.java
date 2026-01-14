package com.example.daam.model;

public class Task {
    public Task() {
    }

    private Long id; // Added ID as it is likely needed, though not explicitly in the partial backend
                     // file shared
    private String description;
    private String status;
    private String clientEmail;
    private String clientName;
    private String workerEmail;
    private String workerName;
    private String createdAt;
    private String confirmedAt;
    private String achievedAt;
    private Long productId;
    private String productName;

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public String getClientName() {
        return clientName;
    }

    public String getWorkerEmail() {
        return workerEmail;
    }

    public String getWorkerName() {
        return workerName;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

}
