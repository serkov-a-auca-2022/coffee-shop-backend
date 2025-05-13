package com.example.coffee_shop_app;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VisitRepository extends JpaRepository<Visit, Long> {

    // Возвращает количество посещений для конкретного userId
    long countByUserId(Long userId);

    @Query("SELECT COALESCE(SUM(v.drinkCount), 0) FROM Visit v WHERE v.userId = :userId")
    long sumDrinkCountByUserId(@Param("userId") Long userId);
}
