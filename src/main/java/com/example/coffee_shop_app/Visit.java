package com.example.coffee_shop_app;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "visits")
public class Visit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Column(name = "visit_time", nullable = false)
    private LocalDateTime visitTime = LocalDateTime.now();

    /** Новое поле: сколько напитков в этом заказе */
    @Column(name = "drink_count", nullable = false)
    private int drinkCount;

    public Visit() { }

    /** Конструктор с указанием userId и количества напитков */
    public Visit(Long userId, int drinkCount) {
        this.userId = userId;
        this.drinkCount = drinkCount;
        this.visitTime = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getVisitTime() {
        return visitTime;
    }
    public void setVisitTime(LocalDateTime visitTime) {
        this.visitTime = visitTime;
    }

    public int getDrinkCount() {
        return drinkCount;
    }
    public void setDrinkCount(int drinkCount) {
        this.drinkCount = drinkCount;
    }
}