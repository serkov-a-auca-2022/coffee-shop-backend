package com.example.coffee_shop_app;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PointsTransactionRepository extends JpaRepository<PointsTransaction, Long> {
    // При желании можно добавить метод получения всех транзакций конкретного пользователя:
    List<PointsTransaction> findByUserId(Long userId);
}
