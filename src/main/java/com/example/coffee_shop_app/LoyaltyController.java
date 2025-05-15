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

    /**
     * GET /api/loyalty/summary/{userId}
     * Возвращает сводку лояльности:
     *  – points          текущее кол-во баллов,
     *  – freeDrinks      доступные бесплатные напитки,
     *  – totalDrinks     сколько напитков всего куплено,
     *  – drinksToNextFree сколько осталось до след. бесплатного напитка.
     */
    @GetMapping("/summary/{userId}")
    public ResponseEntity<?> getSummary(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        long totalDrinks = visitRepository.countByUserId(userId);
        double points     = user.getPoints();
        int    freeDrinks = user.getFreeDrinks();

        long mod = totalDrinks % 6;
        long drinksToNext = (mod == 0 ? 6 : 6 - mod);

        Map<String, Object> result = Map.of(
                "points",           points,
                "freeDrinks",       freeDrinks,
                "totalDrinks",      totalDrinks,
                "drinksToNextFree", drinksToNext
        );

        return ResponseEntity.ok(result);
    }
}
