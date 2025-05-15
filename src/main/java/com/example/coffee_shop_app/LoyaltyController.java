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

        double points     = user.getPoints();
        int    freeDrinks = user.getFreeDrinks();
        int    cycleCount = user.getLoyaltyCount();      // 0…5 покупок в текущем цикле
        int    toNextFree = 6 - cycleCount;              // сколько осталось до бесплатного

        return ResponseEntity.ok(Map.of(
                "points",           points,
                "freeDrinks",       freeDrinks,
                "totalDrinks",      cycleCount,
                "drinksToNextFree", toNextFree
        ));
    }

}
