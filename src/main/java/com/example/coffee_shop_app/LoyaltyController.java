// src/main/java/com/example/coffee_shop_app/LoyaltyController.java
package com.example.coffee_shop_app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/loyalty")
public class LoyaltyController {

    @Autowired
    private VisitRepository visitRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    /**
     * Логировать заказ как «визит» с указанием числа напитков.
     * Если набралось ≥7 напитков, выдаём free-drink и уведомление.
     *
     * POST /api/loyalty/visits/{userId}
     * Body: { "drinkCount": 3 }
     */
    @PostMapping("/visits/{userId}")
    public ResponseEntity<?> addVisit(
            @PathVariable Long userId,
            @RequestBody Map<String, Integer> body
    ) {
        int drinkCount = body.getOrDefault("drinkCount", 1);

        // 1) сохраняем новый визит
        visitRepository.save(new Visit(userId, drinkCount));

        // 2) считаем общее число купленных напитков
        long totalDrinks = visitRepository.sumDrinkCountByUserId(userId);

        // 3) обновляем freeDrinks в User
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        int alreadyFree = user.getFreeDrinks();
        int newFree = (int)(totalDrinks / 7) - alreadyFree;
        if (newFree > 0) {
            user.setFreeDrinks(alreadyFree + newFree);
            userRepository.save(user);

            // 4) создаём уведомление о новом бесплатном напитке
            Notification notif = new Notification(
                    userId,
                    "Поздравляем!",
                    "Вы получили " + newFree + " бесплатный(ых) напиток(ей)!"
            );
            notificationRepository.save(notif);
        }

        // 5) возвращаем клиенту информацию для UI
        return ResponseEntity.ok(Map.of(
                "totalDrinks", totalDrinks,
                "freeDrinks", user.getFreeDrinks()
        ));
    }

    /**
     * Новый эндпоинт: отдаёт сводку лояльности для экрана QR:
     * - points           — текущее значение points у пользователя
     * - freeDrinks       — текущее число freeDrinks
     * - totalDrinks      — всего куплено напитков (сумма drinkCount)
     * - drinksToNextFree — сколько осталось до следующего free drink
     *
     * GET /api/loyalty/summary/{userId}
     */
    @GetMapping("/summary/{userId}")
    public ResponseEntity<?> getSummary(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        long totalDrinks = visitRepository.sumDrinkCountByUserId(userId);
        double points = user.getPoints();
        int freeDrinks = user.getFreeDrinks();
        int mod = (int)(totalDrinks % 7);
        int drinksToNext = 7 - mod;

        return ResponseEntity.ok(Map.of(
                "points", points,
                "freeDrinks", freeDrinks,
                "totalDrinks", totalDrinks,
                "drinksToNextFree", drinksToNext
        ));
    }

    /**
     * Получить все уведомления пользователя
     *
     * GET /api/loyalty/notifications/{userId}
     */
    @GetMapping("/notifications/{userId}")
    public ResponseEntity<?> getUserNotifications(@PathVariable Long userId) {
        return ResponseEntity.ok(
                notificationRepository.findByUserId(userId)
        );
    }
}
