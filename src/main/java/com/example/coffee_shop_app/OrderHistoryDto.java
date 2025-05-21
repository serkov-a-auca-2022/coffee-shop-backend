package com.example.coffee_shop_app;

import java.time.LocalDateTime;

public class OrderHistoryDto {
    private Long orderId;
    private LocalDateTime orderDate;
    private double totalAmount;
    private int itemsCount;

    public OrderHistoryDto(Order o) {
        this.orderId     = o.getId();
        this.orderDate   = o.getOrderDate();
        this.totalAmount = o.getFinalAmount();    // или totalAmount, как вам нужно
        this.itemsCount  = o.getItems().size();
    }

    // геттеры и сеттеры
    public Long getOrderId()      { return orderId; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public double getTotalAmount(){ return totalAmount; }
    public int getItemsCount()    { return itemsCount; }
}
