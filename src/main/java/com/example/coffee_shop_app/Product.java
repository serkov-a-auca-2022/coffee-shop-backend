package com.example.coffee_shop_app;

import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Название товара */
    @Column(nullable = false)
    private String name;

    /** Описание (опционально) */
    @Column(length = 1000)
    private String description;

    /** Цена */
    @Column(nullable = false)
    private Double price;

    /** Категория товара */
    @Column(nullable = false)
    private String category;

    /** URL картинки */
    @Column(name = "image_url")
    private String imageUrl;

    public Product() { }

    public Product(String name, String description, Double price, String category, String imageUrl) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.imageUrl = imageUrl;
    }

    // --- Геттеры / сеттеры ---

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
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

    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
