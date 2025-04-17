package com.example.coffee_shop_app;

import jakarta.persistence.*;

@Entity
@Table(name = "cashiers")
public class Cashier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Имя пользователя (логин)
    @Column(unique = true, nullable = false)
    private String username;

    // Пароль (храните его в зашифрованном виде в реальном проекте)
    @Column(nullable = false)
    private String password;

    // Дополнительное поле для полного имени (опционально)
    private String fullName;

    // Конструктор по умолчанию (обязательный для JPA)
    public Cashier() {
    }

    // Полный конструктор
    public Cashier(String username, String password, String fullName) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    // Обычно сеттер для id не нужен, т.к. значение генерируется автоматически
    // public void setId(Long id) { this.id = id; }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
