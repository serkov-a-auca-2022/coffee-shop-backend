package com.example.coffee_shop_app;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Getter
@Setter
@RestController
@RequestMapping("/api/points")
public class PointsController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PointsHistoryRepository pointsHistoryRepository;

    // ✅ Начисление баллов
    @PostMapping("/add")
    public ResponseEntity<?> addPoints(@RequestBody PointsTransaction request) {
        Optional<User> optionalUser = userRepository.findById(request.getUserId());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setPoints(user.getPoints() + request.getAmount());
            userRepository.save(user);

            // Записываем в историю
            PointsHistory history = new PointsHistory(user, request.getAmount(), "add", request.getDescription());
            pointsHistoryRepository.save(history);

            return ResponseEntity.ok("Баллы успешно начислены!");
        } else {
            return ResponseEntity.badRequest().body("Пользователь не найден");
        }
    }

    // ✅ Списание баллов
    @PostMapping("/deduct")
    public ResponseEntity<?> deductPoints(@RequestBody PointsTransaction request) {
        Optional<User> optionalUser = userRepository.findById(request.getUserId());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getPoints() < request.getAmount()) {
                return ResponseEntity.badRequest().body("Недостаточно баллов!");
            }

            user.setPoints(user.getPoints() - request.getAmount());
            userRepository.save(user);

            // Записываем в историю
            PointsHistory history = new PointsHistory(user, -request.getAmount(), "deduct", request.getDescription());
            pointsHistoryRepository.save(history);

            return ResponseEntity.ok("Баллы успешно списаны!");
        } else {
            return ResponseEntity.badRequest().body("Пользователь не найден");
        }
    }

    // ✅ Получение истории баллов
    @GetMapping("/history/{userId}")
    public ResponseEntity<List<PointsHistory>> getPointsHistory(@PathVariable Long userId) {
        List<PointsHistory> history = pointsHistoryRepository.findByUserId(userId);
        return ResponseEntity.ok(history);
    }
}
