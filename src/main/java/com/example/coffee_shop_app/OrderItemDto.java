package com.example.coffee_shop_app;

public class OrderItemDto {
    private Long productId;
    private int quantity;

    public OrderItemDto() { }

    public OrderItemDto(Long productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() { return productId; }
    public int getQuantity() { return quantity; }
    public void setProductId(Long productId) { this.productId = productId; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
