package com.example.coffee_shop_app;

import java.util.List;

public class OrderRequest {
    private Long userId; // ID пользователя
    private List<OrderItemDto> items; // список позиций заказа
    private int usePoints; // количество баллов для использования
    private boolean useFreeDrink; // применять бесплатный напиток или нет

    public OrderRequest() {
    }

    public OrderRequest(Long userId, List<OrderItemDto> items, int usePoints, boolean useFreeDrink) {
        this.userId = userId;
        this.items = items;
        this.usePoints = usePoints;
        this.useFreeDrink = useFreeDrink;
    }

    public Long getUserId() {
        return userId;
    }

    public List<OrderItemDto> getItems() {
        return items;
    }

    public int getUsePoints() {
        return usePoints;
    }

    public boolean isUseFreeDrink() {
        return useFreeDrink;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setItems(List<OrderItemDto> items) {
        this.items = items;
    }

    public void setUsePoints(int usePoints) {
        this.usePoints = usePoints;
    }

    public void setUseFreeDrink(boolean useFreeDrink) {
        this.useFreeDrink = useFreeDrink;
    }
}
