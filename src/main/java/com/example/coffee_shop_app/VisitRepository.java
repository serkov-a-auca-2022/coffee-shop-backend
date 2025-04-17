package com.example.coffee_shop_app;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitRepository extends JpaRepository<Visit, Long> {

    // Возвращает количество посещений для конкретного userId
    long countByUserId(Long userId);
}
