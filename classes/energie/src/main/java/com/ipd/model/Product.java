package com.ipd.model;

public class Product {
    private String serialNumber; // natural PK
    private String name;
    private String description;
    private String sellerEmail; // FK references Seller.email

    public Product() {}

    public Product(String serialNumber, String name, String description, String sellerEmail) {
        this.serialNumber = serialNumber;
        this.name = name;
        this.description = description;
        this.sellerEmail = sellerEmail;
    }

    // getters & setters
}
