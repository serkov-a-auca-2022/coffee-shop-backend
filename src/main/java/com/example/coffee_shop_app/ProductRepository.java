package com.example.coffee_shop_app;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Все продукты, отсортированные сначала по категории, затем по имени.
     */
    default List<Product> findAllOrderByCategoryAndName() {
        return findAll(Sort.by(
                Sort.Order.asc("category"),
                Sort.Order.asc("name")
        ));
    }
}
