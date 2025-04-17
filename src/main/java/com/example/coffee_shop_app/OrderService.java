package com.example.coffee_shop_app;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private ProductRepository productRepo;
    @Autowired
    private PointsTransactionRepository ptsRepo;
    @Autowired
    private VisitRepository visitRepo;
    @Autowired
    private NotificationRepository notifRepo;

    @Transactional
    public Order createOrder(OrderRequest req) {
        // 1) Найти пользователя
        User user = userRepo.findById(req.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        // 2) Собираем позиции заказа
        List<OrderItem> items = new ArrayList<>();
        double total = 0.0;
        for (OrderItemDto itemDto : req.getItems()) {
            Product prod = productRepo.findById(itemDto.getProductId())
                    .orElseThrow(() -> new NotFoundException("Product not found"));
            double price = prod.getPrice();
            total += price * itemDto.getQuantity();

            OrderItem item = new OrderItem();
            item.setProduct(prod);
            item.setQuantity(itemDto.getQuantity());
            item.setPrice(price);
            items.add(item);
        }

        // 3) Обработка баллов
        double pointsToUse = req.getUsePoints();
        if (pointsToUse > user.getPoints()) {
            pointsToUse = user.getPoints();
        }
        if (pointsToUse > total) {
            pointsToUse = total;
        }

        // 4) Применение бесплатного напитка
        boolean freeUsed = false;
        double freeDiscount = 0.0;
        if (req.isUseFreeDrink() && user.getFreeDrinks() > 0) {
            freeUsed = true;
            double maxPrice = items.stream()
                    .mapToDouble(OrderItem::getPrice)
                    .max()
                    .orElse(0.0);
            freeDiscount = maxPrice;
            if (freeDiscount > total) freeDiscount = total;
            user.setFreeDrinks(user.getFreeDrinks() - 1);
        }

        // 5) Расчет финальной суммы
        double finalAmount = total - pointsToUse - freeDiscount;
        if (finalAmount < 0.0) finalAmount = 0.0;

        // 6) Начисление баллов (например, 5% от finalAmount)
        int pointsEarned = (int) Math.floor(finalAmount * 0.05);

        // Обновляем баланс баллов пользователя
        double newPoints = user.getPoints() - pointsToUse + pointsEarned;
        user.setPoints(newPoints);

        // 7) Создаем объект Order
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setTotalAmount(total);
        order.setFinalAmount(finalAmount);
        order.setPointsUsed(pointsToUse);
        order.setPointsEarned(pointsEarned);
        order.setFreeDrinkUsed(freeUsed);
        for (OrderItem item : items) {
            item.setOrder(order);
        }
        order.setItems(items);
        orderRepo.save(order);

        // 8) Сохраняем историю баллов
        if (pointsToUse > 0) {
            ptsRepo.save(new PointsTransaction(user, -pointsToUse, "REDEEM", order));
        }
        if (pointsEarned > 0) {
            ptsRepo.save(new PointsTransaction(user, pointsEarned, "EARN", order));
        }

        // 9) Регистрируем визит. Используем конструктор с userId.
        visitRepo.save(new Visit(user.getId()));
        long visitCount = visitRepo.countByUserId(user.getId());
        if (visitCount % 7 == 0) {
            user.setFreeDrinks(user.getFreeDrinks() + 1);
            notifRepo.save(new Notification(user.getId(),
                    "Поздравляем!",
                    "Вы получили бесплатный напиток!"));
        }
        return order;
    }

    public List<Order> getOrdersByUser(Long userId) {
        return orderRepo.findByUserId(userId);
    }
}
