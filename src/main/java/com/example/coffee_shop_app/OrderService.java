package com.example.coffee_shop_app;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired private OrderRepository orderRepo;
    @Autowired private UserRepository userRepo;
    @Autowired private ProductRepository productRepo;
    @Autowired private PointsTransactionRepository ptsRepo;
    @Autowired private VisitRepository visitRepo;
    @Autowired private NotificationRepository notifRepo;

    /**
     * 1) Создать или обновить заказ (подтверждение) + всё про баллы / бесплатные напитки / loyaltyCount
     */
    @Transactional
    public Order createOrUpdateOrder(OrderRequest req) {
        // 1) Определяем пользователя (но пока не меняем его)
        User user = null;
        if (req.getUserId() != null) {
            user = userRepo.findById(req.getUserId())
                    .orElseThrow(() -> new NotFoundException("User not found"));
        } else if (req.getUserQrCode() != null) {
            user = userRepo.findByQrCodeNumber(req.getUserQrCode()).orElse(null);
        }

        // 2) Собираем позиции и считаем total
        List<OrderItem> items = new ArrayList<>();
        double total = 0;
        for (OrderItemDto dto : req.getItems()) {
            Product product = productRepo.findById(dto.getProductId())
                    .orElseThrow(() -> new NotFoundException("Product not found"));
            double price = product.getPrice();
            total += price * dto.getQuantity();
            OrderItem it = new OrderItem();
            it.setProduct(product);
            it.setQuantity(dto.getQuantity());
            it.setPrice(price);
            items.add(it);
        }

        // 3) Скидка по free-drink (только вычисляем, но НЕ декрементим у user)
        boolean useFree = Boolean.TRUE.equals(req.getUseFreeDrink());
        double discount = 0;
        if (user != null && useFree && user.getFreeDrinks() > 0) {
            discount = items.stream()
                    .mapToDouble(OrderItem::getPrice)
                    .max().orElse(0);
        }

        // 4) Списание баллов (только вычисляем, но НЕ списываем у user)
        int ptsToUse = req.getPointsToUse() != null ? req.getPointsToUse() : 0;
        if (user != null && ptsToUse > user.getPoints()) {
            ptsToUse = (int)user.getPoints();
        }

        // 5) Итог и начисление (только вычисляем, но не плюсуем/минусиуем user.points)
        double finalAmount = Math.max(0, total - discount - ptsToUse);
        double rawPts = finalAmount * 0.05;
        double earnedPts = Math.round(rawPts * 100.0) / 100.0;

        // 6) Сохраняем заказ со всеми расчётами
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setTotalAmount(total);
        order.setFinalAmount(finalAmount);
        order.setPointsUsed(ptsToUse);
        order.setPointsEarned((int)Math.floor(earnedPts));
        order.setFreeDrinkUsed(discount > 0);
        order.setStatus(req.getStatus() != null
                ? req.getStatus()
                : OrderStatus.CONFIRMED);

        items.forEach(it -> it.setOrder(order));
        order.setItems(items);
        return orderRepo.save(order);
    }
    /**
     * Получить ВСЕ заказы, кроме активных (история)
     */
    public List<Order> getHistoryOrders() {
        return orderRepo.findAll().stream()
                .filter(o -> o.getStatus() != OrderStatus.CONFIRMED)
                .collect(Collectors.toList());
    }

    /**
     * Только активные заказы
     */
    public List<Order> getActiveOrders() {
        return orderRepo.findAll().stream()
                .filter(o -> o.getStatus() == OrderStatus.CONFIRMED)
                .collect(Collectors.toList());
    }

    /**
     * Один заказ по ID
     */
    public Optional<Order> getOrderById(Long orderId) {
        return orderRepo.findById(orderId);
    }

    /**
     * Редактирование (откат и пересчёт позиций)
     */
    @Transactional
    public Order updateOrder(Long orderId, OrderRequest req) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));

        List<OrderItem> items = new ArrayList<>();
        double total = 0.0;
        for (OrderItemDto dto : req.getItems()) {
            Product p = productRepo.findById(dto.getProductId())
                    .orElseThrow(() -> new NotFoundException("Product not found"));
            double price = p.getPrice();
            total += price * dto.getQuantity();

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(p);
            item.setQuantity(dto.getQuantity());
            item.setPrice(price);
            items.add(item);
        }

        order.setItems(items);
        order.setTotalAmount(total);
        order.setFinalAmount(total); // без скидок/баллов при редактировании
        return orderRepo.save(order);
    }

    /**
     * Отменить заказ
     */
    @Transactional
    public Order cancelOrder(Long orderId) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));
        order.setStatus(OrderStatus.CANCELLED);
        return orderRepo.save(order);
    }

    /**
     * Завершить заказ
     */
    @Transactional
    public Order finishOrder(Long orderId) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));

        // Если ещё не завершён и есть привязанный user
        User user = order.getUser();
        if (user != null && order.getStatus() != OrderStatus.FINISHED) {
            // а) Списание ptsUsed
            if (order.getPointsUsed() > 0) {
                user.setPoints(user.getPoints() - order.getPointsUsed());
                ptsRepo.save(new PointsTransaction(
                        user,
                        -order.getPointsUsed(),
                        "deduct",
                        "Списание за заказ №" + orderId,
                        order
                ));
            }
            // б) Начисление ptsEarned
            if (order.getPointsEarned() > 0) {
                user.setPoints(user.getPoints() + order.getPointsEarned());
                ptsRepo.save(new PointsTransaction(
                        user,
                        order.getPointsEarned(),
                        "add",
                        "Начисление за заказ №" + orderId,
                        order
                ));
            }
            // в) freeDrink
            if (order.getFreeDrinkUsed()) {
                user.setFreeDrinks(user.getFreeDrinks() - 1);
            }
            // г) визиты
            int drinkCount = order.getItems().stream()
                    .mapToInt(OrderItem::getQuantity)
                    .sum();
            if (order.getFreeDrinkUsed()) {
                drinkCount = Math.max(0, drinkCount - 1);
            }
            for (int i = 0; i < drinkCount; i++) {
                visitRepo.save(new Visit(user.getId()));
            }
            // д) loyaltyCount → новые freeDrinks
            int updated = user.getLoyaltyCount() + drinkCount;
            int freebies = updated / 6;
            user.setLoyaltyCount(updated % 6);
            if (freebies > 0) {
                user.setFreeDrinks(user.getFreeDrinks() + freebies);
                notifRepo.save(new Notification(
                        user.getId(),
                        "Поздравляем!",
                        "Вы получили бесплатный напиток!"
                ));
            }
            userRepo.save(user);
        }

        order.setStatus(OrderStatus.FINISHED);
        return orderRepo.save(order);
    }


    @Transactional
    public Order assignUserToOrder(Long orderId, Long userId) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found: " + orderId));
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found: " + userId));
        order.setUser(user);
        return orderRepo.save(order);
    }

    /**
     * Спец. метод для cashier-приложения:
     * списание баллов, бесплатных напитков и автоматическое завершение заказа
     */
    @Transactional
    public Order applyRewards(Long orderId, int freeToUse, int pointsToUse) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found: " + orderId));
        User user = order.getUser();
        if (user == null) {
            throw new IllegalArgumentException("Пользователь не привязан к заказу");
        }

        // 1) Валидируем количество бесплатных напитков
        int drinksInOrder = order.getItems().stream()
                .mapToInt(i -> i.getQuantity())
                .sum();
        int freeMax = Math.min(user.getFreeDrinks(), drinksInOrder);
        if (freeToUse < 0 || freeToUse > freeMax) {
            throw new IllegalArgumentException("Неверное число бесплатных напитков");
        }

        // 2) Валидируем баллы
        double ptsMax = Math.min(user.getPoints(), order.getTotalAmount());
        if (pointsToUse < 0 || pointsToUse > ptsMax) {
            throw new IllegalArgumentException("Неверное число баллов");
        }

        // 3) Обновляем заказ
        order.setPointsUsed(pointsToUse);
        order.setFreeDrinkUsed(freeToUse > 0);
        order.setFreeDrinksUsedCount(freeToUse);

        // 4) Считаем скидку по N самым дорогим напиткам
        List<Double> prices = order.getItems().stream()
                .map(OrderItem::getPrice)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());

        double drinksDiscount = 0;
        for (int i = 0; i < freeToUse && i < prices.size(); i++) {
            drinksDiscount += prices.get(i);
        }

        double discount = pointsToUse + drinksDiscount;
        order.setFinalAmount(Math.max(0, order.getTotalAmount() - discount));

        // 4) Списываем баллы у пользователя
        if (pointsToUse > 0) {
            user.setPoints(user.getPoints() - pointsToUse);
            ptsRepo.save(new PointsTransaction(
                    user,
                    -pointsToUse,
                    "deduct",
                    "Списание за заказ №" + orderId,
                    order
            ));
        }

        // 5) Списываем бесплатные напитки
        if (freeToUse > 0) {
            user.setFreeDrinks(user.getFreeDrinks() - freeToUse);
        }

        // 6) Сохраняем пользователя
        userRepo.save(user);

        // 7) Автоматически завершаем заказ
        order.setStatus(OrderStatus.FINISHED);
        Order saved = orderRepo.save(order);

        // 8) Дополнительно уведомление о бесплатном напитке
        if (freeToUse > 0) {
            notifRepo.save(new Notification(
                    user.getId(),
                    "Поздравляем!",
                    "Вы использовали " + freeToUse + " бесплатных напитка(ов)"
            ));
        }

        return saved;
    }

    public List<Order> getOrdersByUser(Long userId) {
        return orderRepo.findByUserId(userId);
    }


}
