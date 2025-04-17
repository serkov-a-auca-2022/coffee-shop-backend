package com.example.coffee_shop_app;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "points_transactions")  // убедитесь, что имя совпадает с реальным именем таблицы
public class PointsTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // связь с пользователем
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // а не userId, чтобы JPA видел связь

    private double amount;
    private String type;        // например "add", "deduct"
    private String description;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;  // если нужно связать операцию с конкретным заказом

    private LocalDateTime timestamp;

    public PointsTransaction() {
        this.timestamp = LocalDateTime.now();
    }

    // Пример конструктора для "add"/"deduct" без описания
    public PointsTransaction(User user, double amount, String type, Order order) {
        this.user = user;
        this.amount = amount;
        this.type = type;
        this.order = order;
        this.timestamp = LocalDateTime.now();
    }

    // Конструктор с явным description
    public PointsTransaction(User user, double amount, String type, String description, Order order) {
        this.user = user;
        this.amount = amount;
        this.type = type;
        this.description = description;
        this.order = order;
        this.timestamp = LocalDateTime.now();
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public Order getOrder() {
        return order;
    }
    public void setOrder(Order order) {
        this.order = order;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
