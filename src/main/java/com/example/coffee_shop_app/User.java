package com.example.coffee_shop_app;

import jakarta.persistence.*;

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

    /** Баллы пользователя */
    private double points = 0.0;

    /** Доступные бесплатные напитки */
    private int freeDrinks = 0;

    /** Уникальный номер QR-кода */
    @Column(unique = true, nullable = false)
    private String qrCodeNumber;

    /** Счётчик купленных напитков в текущем цикле (до бесплатного) */
    private int loyaltyCount = 0;

    public User() {}

    public User(String email,
                String firstName,
                String lastName,
                String phone,
                String qrCodeNumber,
                double points) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.qrCodeNumber = qrCodeNumber;
        this.points = points;
        this.freeDrinks = 0;
        this.loyaltyCount = 0;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
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

    public int getFreeDrinks() {
        return freeDrinks;
    }
    public void setFreeDrinks(int freeDrinks) {
        this.freeDrinks = freeDrinks;
    }

    public String getQrCodeNumber() {
        return qrCodeNumber;
    }
    public void setQrCodeNumber(String qrCodeNumber) {
        this.qrCodeNumber = qrCodeNumber;
    }

    public int getLoyaltyCount() {
        return loyaltyCount;
    }
    public void setLoyaltyCount(int loyaltyCount) {
        this.loyaltyCount = loyaltyCount;
    }
}
