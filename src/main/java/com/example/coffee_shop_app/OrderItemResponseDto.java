package com.example.coffee_shop_app;

public class OrderItemResponseDto {
    private Long productId;
    private String name;
    private String description;
    private Double price;
    private int quantity;

    public OrderItemResponseDto() {
    }

    public OrderItemResponseDto(OrderItem item) {
        this.productId = item.getProduct().getId();
        this.name = item.getProduct().getName();
        this.description = item.getProduct().getDescription();
        this.price = item.getPrice();
        this.quantity = item.getQuantity();
    }

    public Long getProductId() {
        return productId;
    }
    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }
    public void setPrice(Double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
