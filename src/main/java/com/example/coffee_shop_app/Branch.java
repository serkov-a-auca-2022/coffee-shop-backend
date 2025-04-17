package com.example.coffee_shop_app;

import jakarta.persistence.*;
import java.time.LocalTime;

/**
 * Сущность Branch, представляющая информацию о филиале.
 */
@Entity
@Table(name = "branches")
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;      // Условное название/ориентир
    private String address;   // Адрес филиала

    /**
     * Для хранения времени открытия/закрытия в MySQL
     * можно использовать тип TIME в колонке. Hibernate обычно
     * корректно мапит LocalTime -> TIME.
     */
    @Column(columnDefinition = "TIME")
    private LocalTime openTime;

    @Column(columnDefinition = "TIME")
    private LocalTime closeTime;

    private String imageUrl;  // URL изображения филиала

    // ====== Геттеры и сеттеры ======

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public LocalTime getOpenTime() {
        return openTime;
    }

    public LocalTime getCloseTime() {
        return closeTime;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setOpenTime(LocalTime openTime) {
        this.openTime = openTime;
    }

    public void setCloseTime(LocalTime closeTime) {
        this.closeTime = closeTime;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
