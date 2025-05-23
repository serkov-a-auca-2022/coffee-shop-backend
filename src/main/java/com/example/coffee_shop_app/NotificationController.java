package com.example.coffee_shop_app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    @Autowired
    private NotificationRepository notificationRepository;

    @GetMapping("/user/{userId}")
    public List<Notification> getByUser(@PathVariable Long userId) {
        return notificationRepository.findByUserId(userId);
    }
}
