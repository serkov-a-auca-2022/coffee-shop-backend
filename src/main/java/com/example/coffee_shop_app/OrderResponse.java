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
    private List<OrderItemResponseDto> items;

    public OrderResponse() {
    }

    public OrderResponse(Order order) {
        this.orderId        = order.getId();
        this.userId         = order.getUser() != null ? order.getUser().getId() : null;
        this.orderDate      = order.getOrderDate();
        this.totalAmount    = order.getTotalAmount();
        this.finalAmount    = order.getFinalAmount();
        this.pointsUsed     = order.getPointsUsed();
        this.pointsEarned   = order.getPointsEarned();
        this.freeDrinkUsed  = order.getFreeDrinkUsed();
        this.status         = order.getStatus();
        this.items          = order.getItems().stream()
                .map(OrderItemResponseDto::new)
                .collect(Collectors.toList());
    }

    public Long getOrderId() {
        return orderId;
    }
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
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

    public OrderStatus getStatus() {
        return status;
    }
    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public List<OrderItemResponseDto> getItems() {
        return items;
    }
    public void setItems(List<OrderItemResponseDto> items) {
        this.items = items;
    }
}
