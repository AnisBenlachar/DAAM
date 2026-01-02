package com.ipd.model;

public class Location {
    private double latitude;
    private double longitude;
    private String street;
    private String city;
    private String zipcode;
    private String country;
    private String clientEmail; // FK references Client.email

    public Location() {}

    public Location(double latitude, double longitude, String street, String city, String zipcode, String country, String clientEmail) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.street = street;
        this.city = city;
        this.zipcode = zipcode;
        this.country = country;
        this.clientEmail = clientEmail;
    }

    // getters & setters
}
