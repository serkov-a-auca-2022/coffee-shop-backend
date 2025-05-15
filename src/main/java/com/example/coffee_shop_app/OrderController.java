package com.example.coffee_shop_app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository; // если нужен прямой доступ

    /**
     * Создать новый заказ
     */
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody OrderRequest req) {
        Order o = orderService.createOrUpdateOrder(req);
        return ResponseEntity.ok(o);
    }

    /**
     * Обновить (ре-считать позиции) заказ по ID
     */
    @PutMapping("/{orderId}")
    public ResponseEntity<Order> updateOrder(
            @PathVariable Long orderId,
            @RequestBody OrderRequest req
    ) {
        Order o = orderService.updateOrder(orderId, req);
        return ResponseEntity.ok(o);
    }

    /**
     * Завершить заказ (CONFIRMED → FINISHED)
     */
    @PutMapping("/{orderId}/finish")
    public ResponseEntity<Order> finishOrder(@PathVariable Long orderId) {
        Order o = orderService.finishOrder(orderId);
        return ResponseEntity.ok(o);
    }

    /**
     * Отменить заказ (CONFIRMED → CANCELLED)
     */
    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<Order> cancelOrder(@PathVariable Long orderId) {
        Order o = orderService.cancelOrder(orderId);
        return ResponseEntity.ok(o);
    }

    /**
     * Получить все активные (подтверждённые) заказы
     */
    @GetMapping("/active")
    public ResponseEntity<List<Order>> getActive() {
        return ResponseEntity.ok(orderService.getActiveOrders());
    }

    /**
     * Получить всю историю (неактивные) заказы
     */
    @GetMapping("/history")
    public ResponseEntity<List<Order>> getHistory() {
        return ResponseEntity.ok(orderService.getHistoryOrders());
    }

    /**
     * Получить конкретный заказ по ID
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getById(@PathVariable Long orderId) {
        return orderService.getOrderById(orderId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new NotFoundException("Order not found: " + orderId));
    }
}
