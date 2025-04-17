package com.example.coffee_shop_app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loyalty")
public class LoyaltyController {

    @Autowired
    private VisitRepository visitRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    /**
     * Добавить визит (например, при покупке кофе).
     * Если визитов стало кратно 7, создаём уведомление о бесплатном кофе.
     */
    @PostMapping("/visits/{userId}")
    public ResponseEntity<?> addVisit(@PathVariable Long userId) {
        // Сохраняем новый визит
        Visit visit = new Visit(userId);
        visitRepository.save(visit);

        // Считаем, сколько всего визитов у пользователя
        long visitCount = visitRepository.countByUserId(userId);

        // Если достигнуто кратное 7 (7, 14, 21...), создаём уведомление
        if (visitCount % 7 == 0) {
            Notification notification = new Notification(
                    userId,
                    "Поздравляем!",
                    "Вы накопили бесплатный кофе!"
            );
            notificationRepository.save(notification);

            return ResponseEntity.ok("Добавлен визит и создано уведомление о бесплатном кофе! Всего визитов: " + visitCount);
        }

        return ResponseEntity.ok("Визит добавлен. Текущее количество визитов: " + visitCount);
    }

    /**
     * Получить все уведомления пользователя
     */
    @GetMapping("/notifications/{userId}")
    public ResponseEntity<List<Notification>> getUserNotifications(@PathVariable Long userId) {
        List<Notification> notifications = notificationRepository.findByUserId(userId);

        // Добавляем отладочный вывод:
        for (Notification n : notifications) {
            System.out.println("DEBUG => Notification id=" + n.getId()
                    + ", userId=" + n.getUserId()
                    + ", title=" + n.getTitle()
                    + ", message=" + n.getMessage()
                    + ", timestamp=" + n.getTimestamp());
        }

        return ResponseEntity.ok(notifications);
    }


}
