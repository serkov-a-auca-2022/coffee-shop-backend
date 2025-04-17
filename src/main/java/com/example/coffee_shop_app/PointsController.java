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

    // Репозиторий именно для PointsTransaction
    @Autowired
    private PointsTransactionRepository pointsTransactionRepository;

    // ==========================
    // 1) Начисление баллов
    // ==========================
    @PostMapping("/add")
    public ResponseEntity<?> addPoints(@RequestBody PointsTransactionRequest request) {

        // Пытаемся найти пользователя
        Optional<User> optionalUser = userRepository.findById(request.getUserId());
        if (optionalUser.isEmpty()) {
            return ResponseEntity.badRequest().body("Пользователь не найден");
        }
        User user = optionalUser.get();

        // Увеличиваем баланс
        double newBalance = user.getPoints() + request.getAmount();
        user.setPoints(newBalance);
        userRepository.save(user);

        // Создаём запись о транзакции
        PointsTransaction transaction = new PointsTransaction(
                user,
                request.getAmount(),
                "add",  // Тип операции
                request.getDescription(),
                null    // Здесь можно передать Order, если нужно
        );
        pointsTransactionRepository.save(transaction);

        return ResponseEntity.ok("Баллы успешно начислены!");
    }

    // ==========================
    // 2) Списание баллов
    // ==========================
    @PostMapping("/deduct")
    public ResponseEntity<?> deductPoints(@RequestBody PointsTransactionRequest request) {

        // Находим пользователя
        Optional<User> optionalUser = userRepository.findById(request.getUserId());
        if (optionalUser.isEmpty()) {
            return ResponseEntity.badRequest().body("Пользователь не найден");
        }
        User user = optionalUser.get();

        // Проверяем, достаточно ли баллов
        if (user.getPoints() < request.getAmount()) {
            return ResponseEntity.badRequest().body("Недостаточно баллов!");
        }

        // Списываем
        double newBalance = user.getPoints() - request.getAmount();
        user.setPoints(newBalance);
        userRepository.save(user);

        // Пишем транзакцию
        PointsTransaction transaction = new PointsTransaction(
                user,
                -request.getAmount(),  // Сохраняем в БД отрицательное число
                "deduct",
                request.getDescription(),
                null
        );
        pointsTransactionRepository.save(transaction);

        return ResponseEntity.ok("Баллы успешно списаны!");
    }

    // ==========================
    // 3) Получение истории баллов
    // ==========================
    @GetMapping("/history/{userId}")
    public ResponseEntity<List<PointsTransaction>> getPointsHistory(@PathVariable Long userId) {
        // Предположим, в репозитории есть метод findByUserId
        List<PointsTransaction> history = pointsTransactionRepository.findByUserId(userId);
        return ResponseEntity.ok(history);
    }
}
