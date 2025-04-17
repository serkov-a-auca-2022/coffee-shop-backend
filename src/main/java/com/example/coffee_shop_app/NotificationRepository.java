package com.example.coffee_shop_app;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // Список уведомлений для конкретного пользователя
    List<Notification> findByUserId(Long userId);
}
