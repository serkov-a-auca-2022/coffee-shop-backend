package com.example.coffee_shop_app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/cashiers")
public class CashierAuthController {

    @Autowired
    private CashierRepository cashierRepository;

    /**
     * Эндпоинт для входа кассира по логину и паролю.
     * Если логин найден и пароль совпадает – возвращается объект Cashier,
     * иначе возвращаем 401 Unauthorized.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody CashierLoginRequest request) {
        Optional<Cashier> opt = cashierRepository.findByUsername(request.getUsername());
        if (!opt.isPresent()) {
            // Логин не найден
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        Cashier cashier = opt.get();
        // Сравниваем пароль
        if (!cashier.getPassword().equals(request.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        // Всё хорошо - возвращаем объект кассира (или, например, DTO)
        return ResponseEntity.ok(cashier);
    }

    /**
     * (Дополнительно) Эндпоинт для регистрации нового кассира
     * (если вы хотите прямо из приложения создавать кассиров)
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerCashier(@RequestBody Cashier cashier) {
        // Проверяем, не занят ли username
        if (cashierRepository.findByUsername(cashier.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists");
        }
        // Сохраняем нового кассира
        Cashier saved = cashierRepository.save(cashier);
        return ResponseEntity.ok(saved);
    }
}
