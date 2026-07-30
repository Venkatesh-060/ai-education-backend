package com.example.backend.controller;

import com.example.backend.model.Notification;
import com.example.backend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import java.util.List;
import jakarta.validation.Valid;
import com.example.backend.dto.NotificationRequest;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class NotificationController {

    private final NotificationService service;

    @PostMapping
    public Notification create(

            @Valid @RequestBody NotificationRequest request

    ) {

        return service.create(request);

    }

    @GetMapping
    public Page<Notification> getAll(

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size) {

        return service.getAll(page, size);
    }

    @GetMapping("/unread/{userId}")
    public Page<Notification> unread(

            @PathVariable String userId,

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size) {

        return service.unread(userId, page, size);
    }

    @GetMapping("/{id}")
    public Notification get(@PathVariable String id) {
        return service.get(id);
    }

    @PutMapping("/{id}")
    public Notification update(
            @PathVariable String id,
            @RequestBody Notification notification) {

        return service.update(id, notification);
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<String> markRead(@PathVariable String id) {

        service.markRead(id);

        return ResponseEntity.ok("Notification marked as read");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable String id) {

        service.delete(id);

        return ResponseEntity.ok("Notification deleted");
    }

    // Logged-in user notifications

    @GetMapping("/my")
    public ResponseEntity<List<Notification>> myNotifications(

            @RequestParam String userId,
            @RequestParam(required = false) String batchId,
            @RequestParam(required = false) String sessionId) {

        return ResponseEntity.ok(

                service.getMyNotifications(
                        userId,
                        batchId,
                        sessionId)

        );
    }

    // Search

    @GetMapping("/search")
    public List<Notification> search(@RequestParam String keyword) {

        return service.search(keyword);
    }

    // Priority Filter

    @GetMapping("/priority/{priority}")
    public List<Notification> priority(@PathVariable String priority) {

        return service.getByPriority(priority);
    }

    // Recipient Type Filter

    @GetMapping("/recipient/{type}")
    public List<Notification> recipient(@PathVariable String type) {

        return service.getByRecipientType(type);
    }

    // Status Filter

    @GetMapping("/status/{status}")
    public List<Notification> status(@PathVariable String status) {

        return service.getByStatus(status);
    }

}