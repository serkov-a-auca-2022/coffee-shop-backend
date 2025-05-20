package com.example.coffee_shop_app;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Эндпоинт для получения списка всех категорий товаров.
 */
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final ProductRepository productRepo;

    public CategoryController(ProductRepository productRepo) {
        this.productRepo = productRepo;
    }

    /**
     * Возвращает упорядоченный список уникальных категорий.
     */
    @GetMapping
    public List<String> listCategories() {
        return productRepo.findAllOrderByCategoryAndName().stream()
                .map(Product::getCategory)
                .distinct()
                .collect(Collectors.toList());
    }

    @GetMapping("/category/{cat}")
    public List<Product> listBySingleCategory(@PathVariable("cat") String cat) {
        return productRepo.findAll().stream()
                .filter(p -> cat.equalsIgnoreCase(p.getCategory()))
                .collect(Collectors.toList());
    }
}
