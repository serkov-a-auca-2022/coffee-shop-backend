package com.example.coffee_shop_app;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PointsTransaction {
    private Long userId;
    private double amount;
    private String description;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}


