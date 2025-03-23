package com.example.coffee_shop_app;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String firstName;
    private String lastName;
    private String phone;

    private double points = 0;

    @Column(unique = true, nullable = false)
    private String qrCodeNumber; // 6-значное число

    public User() {}

    public User(String email, String firstName, String lastName, String phone, String qrCodeNumber, double points) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.qrCodeNumber = qrCodeNumber;
        this.points = points;

    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }

    public String getQrCodeNumber() {
        return qrCodeNumber;
    }

    public void setQrCodeNumber(String qrCodeNumber) {
        this.qrCodeNumber = qrCodeNumber;
    }
}
