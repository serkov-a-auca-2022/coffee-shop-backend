package com.example.coffee_shop_app;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PointsHistoryRepository extends JpaRepository<PointsHistory, Long> {
    List<PointsHistory> findByUserId(Long userId);
}
