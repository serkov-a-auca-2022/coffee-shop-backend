package com.example.coffee_shop_app;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime orderDate;
    private double totalAmount;
    private double finalAmount;
    private double pointsUsed;
    private int pointsEarned;
    private boolean freeDrinkUsed;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items;

    // Конструктор по умолчанию (обязательный для JPA)
    public Order() {
    }

    // Полный конструктор
    public Order(User user, LocalDateTime orderDate, int totalAmount, int finalAmount,
                 int pointsUsed, int pointsEarned, boolean freeDrinkUsed, List<OrderItem> items) {
        this.user = user;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.finalAmount = finalAmount;
        this.pointsUsed = pointsUsed;
        this.pointsEarned = pointsEarned;
        this.freeDrinkUsed = freeDrinkUsed;
        this.items = items;
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    // Обычно сеттер для id не требуется, так как значение генерируется автоматически
    // public void setId(Long id) {
    //     this.id = id;
    // }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(double finalAmount) {
        this.finalAmount = finalAmount;
    }

    public double getPointsUsed() {
        return pointsUsed;
    }

    public void setPointsUsed(double pointsUsed) {
        this.pointsUsed = pointsUsed;
    }

    public int getPointsEarned() {
        return pointsEarned;
    }

    public void setPointsEarned(int pointsEarned) {
        this.pointsEarned = pointsEarned;
    }

    public boolean isFreeDrinkUsed() {
        return freeDrinkUsed;
    }

    public void setFreeDrinkUsed(boolean freeDrinkUsed) {
        this.freeDrinkUsed = freeDrinkUsed;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    // Переопределённый метод toString() для удобства отладки
    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", user=" + user +
                ", orderDate=" + orderDate +
                ", totalAmount=" + totalAmount +
                ", finalAmount=" + finalAmount +
                ", pointsUsed=" + pointsUsed +
                ", pointsEarned=" + pointsEarned +
                ", freeDrinkUsed=" + freeDrinkUsed +
                ", items=" + items +
                '}';
    }
}
