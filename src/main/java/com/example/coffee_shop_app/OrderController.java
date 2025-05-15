package com.example.coffee_shop_app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired private OrderService orderService;

    /**
     * Подтвердить (создать или обновить) заказ
     */
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest request) {
        Order order = orderService.createOrUpdateOrder(request);
        return ResponseEntity.ok(new OrderResponse(order));
    }

    /**
     * Список активных заказов
     */
    @GetMapping("/active")
    public ResponseEntity<List<OrderResponse>> getActiveOrders() {
        List<OrderResponse> dtos = orderService.getActiveOrders().stream()
                .map(OrderResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    /**
     * История заказов (завершённые и отменённые)
     */
    @GetMapping("/history")
    public ResponseEntity<List<OrderResponse>> getHistoryOrders() {
        List<OrderResponse> dtos = orderService.getHistoryOrders().stream()
                .map(OrderResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    /**
     * Детали одного заказа
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable Long orderId) {
        Order order = orderService.getOrderById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));
        return ResponseEntity.ok(new OrderResponse(order));
    }

    /**
     * Редактировать заказ
     */
    @PutMapping("/{orderId}")
    public ResponseEntity<OrderResponse> updateOrder(
            @PathVariable Long orderId,
            @RequestBody OrderRequest request) {
        Order updated = orderService.updateOrder(orderId, request);
        return ResponseEntity.ok(new OrderResponse(updated));
    }

    /**
     * Отменить заказ
     */
    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<OrderResponse> cancel(@PathVariable Long orderId) {
        Order cancelled = orderService.cancelOrder(orderId);
        return ResponseEntity.ok(new OrderResponse(cancelled));
    }

    /**
     * Завершить заказ
     */
    @PutMapping("/{orderId}/finish")
    public ResponseEntity<OrderResponse> complete(@PathVariable("orderId") Long orderId) {
        Order finished = orderService.finishOrder(orderId);
        return ResponseEntity.ok(new OrderResponse(finished));
    }
}
