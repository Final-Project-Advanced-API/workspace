package org.example.workspaceservice.Client;
import org.example.workspaceservice.model.request.NotificationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification-service",url = "http://localhost:8082")
public interface NotificationClient {
    @PostMapping("/api/v1/notifications")
    ResponseEntity<?> sendNotificationToUser(@RequestBody NotificationRequest notificationRequest);
}
