package com.example.coffee_shop_app;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Репозиторий для Branch
 */
public interface BranchRepository extends JpaRepository<Branch, Long> {
    // Здесь можно добавить дополнительные методы поиска при необходимости
}
