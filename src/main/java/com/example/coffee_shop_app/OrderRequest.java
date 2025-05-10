package com.example.coffee_shop_app;

import java.util.List;

public class OrderRequest {
    private Long userId;              // ID пользователя
    private String userQrCode;        // 6-значный QR-код
    private List<OrderItemDto> items; // Список позиций заказа
    private Integer pointsToUse;      // Количество баллов
    private Boolean useFreeDrink;     // Использовать ли бесплатный напиток
    private OrderStatus status;       // Статус заказа

    // Геттеры и сеттеры
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getUserQrCode() { return userQrCode; }
    public void setUserQrCode(String userQrCode) { this.userQrCode = userQrCode; }
    public List<OrderItemDto> getItems() { return items; }
    public void setItems(List<OrderItemDto> items) { this.items = items; }
    public Integer getPointsToUse() { return pointsToUse; }
    public void setPointsToUse(Integer pointsToUse) { this.pointsToUse = pointsToUse; }
    public Boolean getUseFreeDrink() { return useFreeDrink; }
    public void setUseFreeDrink(Boolean useFreeDrink) { this.useFreeDrink = useFreeDrink; }
    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }
}
