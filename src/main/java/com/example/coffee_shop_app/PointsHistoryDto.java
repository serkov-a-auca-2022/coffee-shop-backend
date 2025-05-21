package com.example.coffee_shop_app;

import java.time.LocalDateTime;

public class PointsHistoryDto {
    private double amount;              // плюс/минус баллов
    private String description;         // текст операции
    private LocalDateTime timestamp;    // когда
    private String type;                // "add" или "deduct"

    public PointsHistoryDto(double amount, String description, LocalDateTime timestamp, String type) {
        this.amount      = amount;
        this.description = description;
        this.timestamp   = timestamp;
        this.type        = type;
    }

    // геттеры
    public double getAmount()           { return amount; }
    public String getDescription()      { return description; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getType()             { return type; }
}
