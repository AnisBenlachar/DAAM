package com.example.daam.model;

public class CreateTaskRequest {
    private String description;
    private String clientEmail;
    private String status;
    private String workerEmail;
    private Long productId;

    public CreateTaskRequest() {
    }

    public CreateTaskRequest(String description, String clientEmail, String status, String workerEmail,
            Long productId) {
        this.description = description;
        this.clientEmail = clientEmail;
        this.status = status;
        this.workerEmail = workerEmail;
        this.productId = productId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWorkerEmail() {
        return workerEmail;
    }

    public void setWorkerEmail(String workerEmail) {
        this.workerEmail = workerEmail;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
