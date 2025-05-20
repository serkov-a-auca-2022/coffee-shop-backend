package com.example.coffee_shop_app;

import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Эндпоинты по продуктам.
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductRepository productRepo;

    public ProductController(ProductRepository productRepo) {
        this.productRepo = productRepo;
    }

    /**
     * Плоский список всех товаров, отсортированных по category/name.
     */
    @GetMapping
    public List<Product> listAll() {
        return productRepo.findAllOrderByCategoryAndName();
    }

    /**
     * Сгруппированный по категории список:
     * {
     *   "Кофе":    [ {...}, {...} ],
     *   "Чай":     [ {...}, {...} ],
     *   ...
     * }
     */
    @GetMapping("/by-category")
    public Map<String, List<Product>> listByCategory() {
        List<Product> all = productRepo.findAllOrderByCategoryAndName();
        Map<String, List<Product>> grouped = new LinkedHashMap<>();
        for (Product p : all) {
            grouped
                    .computeIfAbsent(p.getCategory(), k -> new ArrayList<>())
                    .add(p);
        }
        return grouped;
    }

    /**
     * Список продуктов одной категории.
     */
    @GetMapping("/category/{cat}")
    public List<Product> listForCategory(@PathVariable("cat") String cat) {
        return productRepo.findByCategoryOrderByName(cat);
    }
}
