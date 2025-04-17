package com.example.coffee_shop_app;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CashierRepository extends JpaRepository<Cashier, Long> {
    Optional<Cashier> findByUsername(String username);
}
