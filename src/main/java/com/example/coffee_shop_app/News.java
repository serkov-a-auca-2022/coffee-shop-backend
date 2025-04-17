package com.example.coffee_shop_app;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "news")
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;             // Заголовок
    private String shortDescription;  // Короткое описание (для карточки)

    @Column(columnDefinition = "TEXT")
    private String content;           // Основной текст/контент новости

    private String imageUrl;          // Ссылка на картинку

    private LocalDateTime dateTime;   // Дата и время публикации

    // ----- Конструкторы -----

    public News() {
        // пустой конструктор для JPA
    }

    // Если хотите сразу устанавливать дату при создании в коде:
    public News(String title, String shortDescription, String content, String imageUrl) {
        this.title = title;
        this.shortDescription = shortDescription;
        this.content = content;
        this.imageUrl = imageUrl;
        this.dateTime = LocalDateTime.now();
    }

    // ----- Геттеры и сеттеры -----
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
