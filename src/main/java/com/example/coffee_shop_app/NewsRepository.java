package com.example.coffee_shop_app;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
    // Если нужно, можно добавить дополнительные методы поиска
}
