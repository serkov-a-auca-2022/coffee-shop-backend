package com.example.coffee_shop_app;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {
    private Long id;
    private Long userId;
    private LocalDateTime orderDate;
    private double totalAmount;
    private double finalAmount;
    private double pointsUsed;
    private int pointsEarned;
    private boolean freeDrinkUsed;
    private List<OrderItemDto> items;

    public OrderResponse(Order order) {
        this.id = order.getId();
        this.userId = order.getUser() != null ? order.getUser().getId() : null;
        this.orderDate = order.getOrderDate();
        this.totalAmount = order.getTotalAmount();
        this.finalAmount = order.getFinalAmount();
        this.pointsUsed = order.getPointsUsed();
        this.pointsEarned = order.getPointsEarned();
        this.freeDrinkUsed = order.isFreeDrinkUsed();
        this.items = order.getItems().stream()
                .map(item -> new OrderItemDto(item.getProduct().getId(), item.getQuantity()))
                .collect(Collectors.toList());
    }

    // Геттеры и, при необходимости, сеттеры
    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public double getTotalAmount() { return totalAmount; }
    public double getFinalAmount() { return finalAmount; }
    public double getPointsUsed() { return pointsUsed; }
    public int getPointsEarned() { return pointsEarned; }
    public boolean isFreeDrinkUsed() { return freeDrinkUsed; }
    public List<OrderItemDto> getItems() { return items; }
}
