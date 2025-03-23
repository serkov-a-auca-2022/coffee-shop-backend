package com.example.coffee_shop_app;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByPhone(String phone);
    Optional<User> findByQrCodeNumber(String qrCodeNumber); // Поиск по QR-коду
}

