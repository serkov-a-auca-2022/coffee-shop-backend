package com.example.coffee_shop_app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // Генерация уникального 6-значного номера
    private String generateUniqueQrCode() {
        Random random = new Random();
        String qrCode;
        do {
            qrCode = String.format("%06d", random.nextInt(999999));
        } while (userRepository.findByQrCodeNumber(qrCode).isPresent());
        return qrCode;
    }

    // Регистрация нового пользователя
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        if (userRepository.findByPhone(user.getPhone()).isPresent()) {
            return ResponseEntity.badRequest().body("Этот номер телефона уже зарегистрирован");
        }

        user.setQrCodeNumber(generateUniqueQrCode()); // Генерируем QR-код
        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(savedUser);
    }

    // Получение пользователя по телефону
    @GetMapping("/{phone}")
    public ResponseEntity<User> getUser(@PathVariable String phone) {
        return userRepository.findByPhone(phone)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/qr/{code}")
    public ResponseEntity<User> getUserByQr(@PathVariable String code) {
        return userRepository.findByQrCodeNumber(code)
                .map(user -> ResponseEntity.ok(user))
                .orElse(ResponseEntity.notFound().build());
    }
}
