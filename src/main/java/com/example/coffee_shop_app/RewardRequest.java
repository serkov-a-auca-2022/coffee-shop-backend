package com.example.coffee_shop_app;

public class RewardRequest {
    private Long orderId;
    private int useFreeDrinks;
    private int usePoints;

    // Геттеры и сеттеры
    public Long getOrderId() {
        return orderId;
    }
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public int getUseFreeDrinks() {
        return useFreeDrinks;
    }
    public void setUseFreeDrinks(int useFreeDrinks) {
        this.useFreeDrinks = useFreeDrinks;
    }

    public int getUsePoints() {
        return usePoints;
    }
    public void setUsePoints(int usePoints) {
        this.usePoints = usePoints;
    }
}
