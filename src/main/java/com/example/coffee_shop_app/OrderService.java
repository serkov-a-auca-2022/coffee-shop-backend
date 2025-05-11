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
     * Создать или обновить заказ (подтверждение)
     */
    @Transactional
    public Order createOrUpdateOrder(OrderRequest req) {
        // 1) Определить пользователя по ID или по QR-коду
        User user = null;
        if (req.getUserId() != null) {
            user = userRepo.findById(req.getUserId())
                    .orElseThrow(() -> new NotFoundException("User not found"));
        } else if (req.getUserQrCode() != null) {
            user = userRepo.findByQrCodeNumber(req.getUserQrCode()).orElse(null);
        }

        // 2) Собрать позиции и посчитать total
        List<OrderItem> items = new ArrayList<>();
        double total = 0;
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

        // 3) Обработка бесплатного напитка
        boolean useFree = req.getUseFreeDrink() != null && req.getUseFreeDrink();
        double discount = 0;
        if (user != null && useFree && user.getFreeDrinks() > 0) {
            discount = items.stream()
                    .mapToDouble(OrderItem::getPrice)
                    .max()
                    .orElse(0);
            user.setFreeDrinks(user.getFreeDrinks() - 1);
        }

        // 4) Применение баллов
        int pointsToUse = req.getPointsToUse() != null ? req.getPointsToUse() : 0;
        if (user != null && pointsToUse > user.getPoints()) {
            pointsToUse = (int) user.getPoints();
        }

        // 5) Расчёт итоговой суммы и начисление баллов
        double finalAmount = total - discount - pointsToUse;
        if (finalAmount < 0) finalAmount = 0;
        int pointsEarned = (int) Math.floor(finalAmount * 0.05);
        if (user != null) {
            user.setPoints(user.getPoints() - pointsToUse + pointsEarned);
        }

        // 6) Сборка и сохранение заказа
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setTotalAmount(total);
        order.setFinalAmount(finalAmount);
        order.setPointsUsed(pointsToUse);
        order.setPointsEarned(pointsEarned);
        order.setFreeDrinkUsed(discount > 0);
        // По умолчанию — ACTIVE (активный)
        order.setStatus(req.getStatus() != null ? req.getStatus() : OrderStatus.CONFIRMED);

        for (OrderItem item : items) {
            item.setOrder(order);
        }
        order.setItems(items);

        orderRepo.save(order);

        // 7) История баллов и визитов
        if (user != null) {
            if (pointsToUse > 0) {
                ptsRepo.save(new PointsTransaction(user, -pointsToUse, "REDEEM", order));
            }
            if (pointsEarned > 0) {
                ptsRepo.save(new PointsTransaction(user, pointsEarned, "EARN", order));
            }

            visitRepo.save(new Visit(user.getId()));
            long visitCount = visitRepo.countByUserId(user.getId());
            if (visitCount % 7 == 0) {
                user.setFreeDrinks(user.getFreeDrinks() + 1);
                notifRepo.save(new Notification(
                        user.getId(),
                        "Поздравляем!",
                        "Вы получили бесплатный напиток!"
                ));
            }
        }

        return order;
    }

    /**
     * Все заказы кроме активных (история — завершённые и отменённые)
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
     * Получить один заказ по ID
     */
    public Optional<Order> getOrderById(Long orderId) {
        return orderRepo.findById(orderId);
    }

    /**
     * Откат позиций (редактирование)
     */
    @Transactional
    public Order updateOrder(Long orderId, OrderRequest req) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));

        List<OrderItem> items = new ArrayList<>();
        double total = 0;
        for (OrderItemDto dto : req.getItems()) {
            Product product = productRepo.findById(dto.getProductId())
                    .orElseThrow(() -> new NotFoundException("Product not found"));
            double price = product.getPrice();
            total += price * dto.getQuantity();

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(dto.getQuantity());
            item.setPrice(price);
            items.add(item);
        }

        order.setItems(items);
        order.setTotalAmount(total);
        order.setFinalAmount(total); // без скидок/баллов
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
}
