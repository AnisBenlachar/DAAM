package com.example.daam.model;

import java.math.BigDecimal;

public class Product {
    private Long id;
    private String name;
    private String description;
    private String category;
    private BigDecimal basePrice;
    private String unit;

    public Product() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public String getUnit() {
        return unit;
    }
}
