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

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest request) {
        Order order = orderService.createOrUpdateOrder(request);
        return ResponseEntity.ok(new OrderResponse(order));
    }

    @GetMapping("/active")
    public ResponseEntity<List<OrderResponse>> getActiveOrders() {
        List<Order> orders = orderService.getActiveOrders();
        return ResponseEntity.ok(orders.stream().map(this::convertToResponse).collect(Collectors.toList()));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponse>> getUserOrders(@PathVariable Long userId) {
        List<Order> orders = orderService.getOrdersByUser(userId);
        return ResponseEntity.ok(orders.stream().map(this::convertToResponse).collect(Collectors.toList()));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable Long orderId) {
        Order order = orderService.getOrderById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));
        return ResponseEntity.ok(convertToResponse(order));
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<OrderResponse> updateOrder(@PathVariable Long orderId, @RequestBody OrderRequest request) {
        Order updated = orderService.updateOrder(orderId, request);
        return ResponseEntity.ok(convertToResponse(updated));
    }

    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<OrderResponse> cancel(@PathVariable Long orderId) {
        Order cancelled = orderService.cancelOrder(orderId);
        return ResponseEntity.ok(convertToResponse(cancelled));
    }

    @PostMapping("/{orderId}/finish")
    public ResponseEntity<OrderResponse> finish(@PathVariable Long orderId) {
        Order finished = orderService.finishOrder(orderId);
        return ResponseEntity.ok(convertToResponse(finished));
    }

    private OrderResponse convertToResponse(Order order) {
        return new OrderResponse(order);
    }
}
