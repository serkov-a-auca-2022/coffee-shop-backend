package com.example.coffee_shop_app;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
        // --- 1) Определяем пользователя ---
        User user = null;
        if (req.getUserId() != null) {
            user = userRepo.findById(req.getUserId())
                    .orElseThrow(() -> new NotFoundException("User not found"));
        } else if (req.getUserQrCode() != null) {
            user = userRepo.findByQrCodeNumber(req.getUserQrCode())
                    .orElse(null);
        }

        // --- 2) Собираем позиции и считаем total ---
        List<OrderItem> items = new ArrayList<>();
        double total = 0.0;
        for (OrderItemDto dto : req.getItems()) {
            Product product = productRepo.findById(dto.getProductId())
                    .orElseThrow(() -> new NotFoundException("Product not found"));
            double price = product.getPrice();
            total += price * dto.getQuantity();

            OrderItem item = new OrderItem();
            item.setProduct(product);
            item.setQuantity(dto.getQuantity());
            item.setPrice(price);
            items.add(item);
        }

        // --- 3) Скидка по бесплатному напитку ---
        boolean useFree = Boolean.TRUE.equals(req.getUseFreeDrink());
        double discount = 0.0;
        if (user != null && useFree && user.getFreeDrinks() > 0) {
            discount = items.stream()
                    .mapToDouble(OrderItem::getPrice)
                    .max().orElse(0.0);
            user.setFreeDrinks(user.getFreeDrinks() - 1);
        }

        // --- 4) Списание баллов ---
        int ptsToUse = req.getPointsToUse() != null ? req.getPointsToUse() : 0;
        if (user != null && ptsToUse > user.getPoints()) {
            ptsToUse = (int) user.getPoints();
        }

        // --- 5) Итоговая сумма и начисление баллов (5%) ---
        double finalAmount = Math.max(0.0, total - discount - ptsToUse);
        double rawPoints   = finalAmount * 0.05;
        double earnedPts   = Math.round(rawPoints * 100.0) / 100.0;

        if (user != null) {
            user.setPoints(user.getPoints() - ptsToUse + earnedPts);
        }

        // --- 6) Формируем и сохраняем заказ ---
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setTotalAmount(total);
        order.setFinalAmount(finalAmount);
        order.setPointsUsed(ptsToUse);
        order.setPointsEarned((int) Math.floor(earnedPts));
        order.setFreeDrinkUsed(discount > 0);
        order.setStatus(req.getStatus() != null
                ? req.getStatus()
                : OrderStatus.CONFIRMED);

        for (OrderItem it : items) {
            it.setOrder(order);
        }
        order.setItems(items);
        order = orderRepo.save(order);

        if (user != null) {
            // 7) Транзакции по баллам
            if (ptsToUse > 0) {
                ptsRepo.save(new PointsTransaction(
                        user, -ptsToUse, "deduct",
                        "Списание за заказ №" + order.getId(), order));
            }
            if (earnedPts > 0) {
                ptsRepo.save(new PointsTransaction(
                        user, earnedPts, "add",
                        "Начисление за заказ №" + order.getId(), order));
            }

            // 8) Каждая оплаченная единица напитка = 1 визит
            int drinkCount = items.stream()
                    .mapToInt(OrderItem::getQuantity)
                    .sum();
            if (order.getFreeDrinkUsed()) {
                // бесплатный напиток из заказа не учитываем
                drinkCount = Math.max(0, drinkCount - 1);
            }
            for (int i = 0; i < drinkCount; i++) {
                visitRepo.save(new Visit(user.getId()));
            }

            // 9) Обновляем loyaltyCount и выдаём freeDrinks каждые 6 напитков
            int updatedCount = user.getLoyaltyCount() + drinkCount;
            int newFree      = updatedCount / 6;      // сколько полных «пакетов» по 6
            user.setLoyaltyCount(updatedCount % 6);   // остаток после выдачи
            if (newFree > 0) {
                user.setFreeDrinks(user.getFreeDrinks() + newFree);
                notifRepo.save(new Notification(
                        user.getId(),
                        "Поздравляем!",
                        "Вы получили бесплатный напиток!"
                ));
            }

            userRepo.save(user);
        }

        return order;
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

}
