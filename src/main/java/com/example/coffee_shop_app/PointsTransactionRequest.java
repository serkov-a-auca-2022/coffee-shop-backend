package com.example.coffee_shop_app;

// DTO для передачи данных о начислении или списании баллов
public class PointsTransactionRequest {
    private Long userId;
    private double amount;
    private String description;

    public PointsTransactionRequest() {
    }

    public PointsTransactionRequest(Long userId, double amount, String description) {
        this.userId = userId;
        this.amount = amount;
        this.description = description;
    }

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
