package com.example.coffee_shop_app;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {
    private Long orderId;
    private Long userId;
    private LocalDateTime orderDate;
    private double totalAmount;
    private double finalAmount;
    private double pointsUsed;
    private int pointsEarned;
    private boolean freeDrinkUsed;
    private OrderStatus status;
    private List<OrderItemDto> items;

    // Конструктор из Order
    public OrderResponse(Order order) {
        this.orderId = order.getId();
        this.userId = order.getUser() != null ? order.getUser().getId() : null;
        this.orderDate = order.getOrderDate();
        this.totalAmount = order.getTotalAmount();
        this.finalAmount = order.getFinalAmount();
        this.pointsUsed = order.getPointsUsed();
        this.pointsEarned = order.getPointsEarned();
        this.freeDrinkUsed = order.getFreeDrinkUsed();
        this.status = order.getStatus();
        this.items = order.getItems().stream()
                .map(item -> new OrderItemDto(item.getProduct().getId(), item.getQuantity()))
                .collect(Collectors.toList());
    }

    // Геттеры и сеттеры
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    public double getFinalAmount() { return finalAmount; }
    public void setFinalAmount(double finalAmount) { this.finalAmount = finalAmount; }
    public double getPointsUsed() { return pointsUsed; }
    public void setPointsUsed(double pointsUsed) { this.pointsUsed = pointsUsed; }
    public int getPointsEarned() { return pointsEarned; }
    public void setPointsEarned(int pointsEarned) { this.pointsEarned = pointsEarned; }
    public boolean getFreeDrinkUsed() { return freeDrinkUsed; }
    public void setFreeDrinkUsed(boolean freeDrinkUsed) { this.freeDrinkUsed = freeDrinkUsed; }
    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }
    public List<OrderItemDto> getItems() { return items; }
    public void setItems(List<OrderItemDto> items) { this.items = items; }
}
